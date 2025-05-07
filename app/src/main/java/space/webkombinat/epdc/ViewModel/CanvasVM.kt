package space.webkombinat.epdc.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.RawValue
import space.webkombinat.epdc.Model.CanvasObjects.RectData
import space.webkombinat.epdc.Model.Controller.CanvasManager
import space.webkombinat.epdc.Model.CanvasObjects.TextDate
import space.webkombinat.epdc.Model.ColorSet
import space.webkombinat.epdc.Model.Controller.FontFolderReader
import space.webkombinat.epdc.Model.Controller.UsbController
import javax.inject.Inject

@HiltViewModel
class CanvasVM @Inject constructor(
    val canvasManager: CanvasManager,
    val usbController: UsbController,
    val fontFolderReader: FontFolderReader,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = savedStateHandle.getStateFlow("uiStateKey", UiState())
    val uiState: StateFlow<UiState> = _uiState

    var black_previewPixelList =  mutableStateListOf<Int>()
    var red_previewPixelList = mutableStateListOf<Int>()
    var usbButtonState = mutableStateOf(true)
    var convertButtonState = mutableStateOf(true)

    @Parcelize
    data class UiState(
        val itemCounter: Int = 0,
        val selectTab: ColorMode = ColorMode.Black,
        val selectSideList: OperateType = OperateType.Text,
        val operateType: OperateType = OperateType.Text,
        val operateIndex: Int = 0,
        val textItems: List<TextDate> = emptyList(),
        val rectItems: List<RectData> = emptyList()
    ): Parcelable

    fun set_openList(mode: OperateType) {
        val newData =  _uiState.value.copy(selectSideList = mode)
        savedStateHandle["uiStateKey"] = newData
    }

    fun setTabMode(mode: ColorMode) {
        val newData =  _uiState.value.copy(selectTab = mode)
        savedStateHandle["uiStateKey"] = newData
    }

    fun addItemCounter() {
        val newData =  _uiState.value.copy(itemCounter = _uiState.value.itemCounter + 1)
        savedStateHandle["uiStateKey"] = newData
    }

    fun getScreenSize(ctx: Context): Triple<Int, Int, Int>{
        val canvasWidth = pxToDp(canvasManager.virual_EPD_W, ctx)
        val canvasHeight = pxToDp(canvasManager.virual_EPD_H, ctx)
        val maskSize = pxToDp(
            canvasManager.screenWidth - canvasManager.virual_EPD_W,
            ctx = ctx
        ) / 2
        return Triple(canvasWidth, canvasHeight, maskSize)
    }

    fun usbCommunicationSetup(){
        usbController.setup()
    }
    fun usbConnectionSetup(ctx: Context) {
        usbController.getDevice(ctx = ctx)
    }
    fun usbDataTransfer() {
        if (usbButtonState.value) {
            usbButtonState.value = false
            val th = Thread{
                if (black_previewPixelList.isNotEmpty() && red_previewPixelList.isNotEmpty()) {
                    val transferDataByte = canvasManager.listIntToByteArrayDirect(black_previewPixelList)

                    val convertZeroToOne = canvasManager.redZeroToOne(red_previewPixelList)
                    val transferDataByte_red = canvasManager.listIntToByteArrayDirect(convertZeroToOne)

                    println("re array size ${transferDataByte_red.size}${transferDataByte[0]}")
                    println("bl array size ${transferDataByte.size}${transferDataByte[0]}")
                    if(transferDataByte.size == 4736 && transferDataByte_red.size == 4736) {
                        try {
                            usbController.transferData(transferDataByte)
                            Thread.sleep(100)
                            usbController.transferData(transferDataByte_red)
                            println("red transferd")
                            Thread.sleep(5000)
                            usbButtonState.value = true
                        } catch (e: Exception) {
                            println("sleep miss ${e}")
                            usbButtonState.value = true
                        }
                    } else {
                        println("transferDataByte size error 配列の変換ミスかも")
                        usbButtonState.value = true
                    }

                }
            }
            th.start()
        }
    }

    fun convert(bitmap: Bitmap, rect: Rect) {
        convertButtonState.value = false
        val newBitmap = Bitmap.createBitmap(
                bitmap,
                rect.left,
                rect.top,
                rect.width(),
                rect.height()
        )
//        println("newBitmap width = ${newBitmap.width} height = ${newBitmap.height}")
        when(_uiState.value.selectTab) {
            ColorMode.Black -> {
                black_previewPixelList.clear()
                viewModelScope.launch {
                    val newList = canvasManager.bitmapToHexList(bitmap = newBitmap)
                    println("hoge length ${convertButtonState.value}")
                    black_previewPixelList.addAll(newList)
                    convertButtonState.value = true
                }
            }
            ColorMode.Red -> {
                red_previewPixelList.clear()
                viewModelScope.launch {
                    val newList = canvasManager.bitmapToHexList(bitmap = newBitmap)
//                    println("hoge length ${hoge.size}")
                    red_previewPixelList.addAll(newList)
                    convertButtonState.value = true
                }
            }
        }

    }

    fun setOperateType(mode: OperateType) {
        val newData =  _uiState.value.copy(operateType = mode)
        savedStateHandle["uiStateKey"] = newData
    }
    fun setOperateIndex(index: Int) {
        val newData =  _uiState.value.copy(operateIndex = index)
        savedStateHandle["uiStateKey"] = newData
    }
    fun change(num: Int, operateType: OperateType) {
        setOperateType(mode = operateType)
        when(operateType){
            OperateType.Text -> {
                val i = uiState.value.textItems.indexOfFirst{ it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    setOperateIndex(index = i+1)
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }
            OperateType.Rect -> {
                val i = uiState.value.rectItems.indexOfFirst{ it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    setOperateIndex(index = i+1)
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }
//            OperateType.Circle -> {}
        }
        println("change id ${num}")

    }

    fun addText() {
        addItemCounter()
        var newText = TextDate(
            id = uiState.value.itemCounter,
            text = "dammy",
            x = 0,
            y = 0,
            fontSize = 50,
            fontWeight = 300,
            fontFamily = FontFamily.Default.toString(),
            colorMode = _uiState.value.selectTab
        )
        val updatedTextItems = uiState.value.textItems + newText
        savedStateHandle["uiStateKey"] =
            uiState.value.copy(
                textItems = updatedTextItems,
                operateType = OperateType.Text
            )
        setOperateIndex(uiState.value.textItems.size)
    }
    fun updateText(newText: String) {
        val currentData = uiState.value.textItems[uiState.value.operateIndex-1]
        val newData = currentData.copy(text = newText)
        val updateTextItems = uiState.value.textItems.map { if (it.id == currentData.id) newData else it }
        savedStateHandle["uiStateKey"] = uiState.value.copy(textItems = updateTextItems)
    }

    fun removeText(targetId: Int){
        val updatedTextItems = uiState.value.textItems.filter { it.id != targetId }
        val length = updatedTextItems.size-1
        if (length > 0){
            setOperateIndex(length)
        } else {
            setOperateIndex(1)
        }
        setOperateType(OperateType.Text)
        savedStateHandle["uiStateKey"] = uiState.value.copy(textItems = updatedTextItems)
    }

    fun addRect() {
        addItemCounter()
        val newRect = RectData(
            id = uiState.value.itemCounter,
            x = 0,
            y = 0,
            size_h = 50,
            size_w = 50,
            degree = 0,
            color = 1,
            colorMode = uiState.value.selectTab
        )
        val updatedRectItems = uiState.value.rectItems + newRect
        savedStateHandle["uiStateKey"] =
            uiState.value.copy(
                rectItems = updatedRectItems,
                operateType = OperateType.Rect
            )
        setOperateIndex(uiState.value.rectItems.size)
    }

    fun removeRect(targetId: Int) {
        val updatedRectItems = uiState.value.rectItems.filter { it.id != targetId }
        val length = updatedRectItems.size-1
        if (length > 0){
            setOperateIndex(length)
        } else {
            setOperateIndex(1)
        }
        setOperateType(OperateType.Rect)
        savedStateHandle["uiStateKey"] = uiState.value.copy(rectItems = updatedRectItems)
    }

    fun rectParameterUpdate(
        updateParameter: Rect_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = uiState.value.rectItems[uiState.value.operateIndex-1]
        val updateFunction: (Int) -> Int = when(sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrementRect(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrementRect(updateParameter) }
        }
        val newData = when (updateParameter) {
            Rect_Parameter.X -> currentData.copy(x = updateFunction(currentData.x))
            Rect_Parameter.Y -> currentData.copy(y = updateFunction(currentData.y))
            Rect_Parameter.WIDTH -> currentData.copy(size_w = updateFunction(currentData.size_w))
            Rect_Parameter.HEIGHT ->  currentData.copy(size_h = updateFunction(currentData.size_h))
            Rect_Parameter.DEGREE -> currentData.copy(degree = updateFunction(currentData.degree))
        }
        val updateRectItems = uiState.value.rectItems.map { if (it.id == currentData.id) newData else it }
        savedStateHandle["uiStateKey"] = uiState.value.copy(rectItems = updateRectItems)
    }

    fun textParameterUpdate(
        updateParameter: Text_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = uiState.value.textItems[uiState.value.operateIndex-1]
        val updateFunction: (Int) -> Int = when(sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrementText(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrementText(updateParameter) }
        }
        val newData = when (updateParameter) {
            Text_Parameter.Weight -> {
                val newValue = updateFunction(currentData.fontWeight)
                if (newValue in 200 .. 900) { // Weight範囲チェック
                    currentData.copy(fontWeight = newValue)
                } else {
                    return
                }
            }
            Text_Parameter.Size -> currentData.copy(fontSize = updateFunction(currentData.fontSize))
            Text_Parameter.X -> currentData.copy(x = updateFunction(currentData.x))
            Text_Parameter.Y -> currentData.copy(y = updateFunction(currentData.y))
        }

        val updateTextItems = uiState.value.textItems.map { if (it.id == currentData.id) newData else it }
        savedStateHandle["uiStateKey"] = uiState.value.copy(textItems = updateTextItems)
    }

    private fun getIncrementText(parameter: Text_Parameter): Int = when (parameter) {
        Text_Parameter.Weight -> 100
        Text_Parameter.Size -> 10
        Text_Parameter.X -> 10
        Text_Parameter.Y -> 10
    }

    private fun getIncrementRect(parameter: Rect_Parameter): Int = when (parameter) {
        Rect_Parameter.X -> 10
        Rect_Parameter.Y -> 10
        Rect_Parameter.WIDTH -> 10
        Rect_Parameter.HEIGHT -> 10
        Rect_Parameter.DEGREE -> 10
    }


    fun pxToDp(size: Int, ctx: Context): Int {
        val density = ctx.resources.displayMetrics.density
        return(size / density).toInt()
    }

    fun getFont(ctx: Context, fontName:String): FontFamily {
        when(fontName) {
            FontFamily.Default.toString() -> return FontFamily.Default
            FontFamily.Monospace.toString() -> return FontFamily.Monospace
            FontFamily.Serif.toString() -> return FontFamily.Serif
            FontFamily.SansSerif.toString() -> return FontFamily.SansSerif
            else -> {
                return fontFolderReader.getFontFamily(ctx = ctx, fileName = fontName)
            }
        }
    }
    fun checkFont(fontName:String): Boolean {
        return when(fontName) {
            FontFamily.Default.toString() -> true
            FontFamily.Monospace.toString() -> true
            FontFamily.Serif.toString() -> true
            FontFamily.SansSerif.toString() -> true
            else -> {
                false
            }
        }
    }
    fun changeFont(ctx: Context,fontName:String) {
        var font: String
        if (checkFont(fontName)) {
            font = getFont(ctx, fontName).toString()
        } else {
            font = fontName
        }
        val currentData = uiState.value.textItems[uiState.value.operateIndex-1]
        val newData = currentData.copy(fontFamily = font)
        savedStateHandle["uiStateKey"] = uiState.value.copy(textItems = uiState.value.textItems
            .map { if (it.id == currentData.id) newData else it })
    }
}

sealed class ColorMode: Parcelable  {
    @Parcelize
    object Black: ColorMode() {
        @IgnoredOnParcel
        override val name = "黒"
        @IgnoredOnParcel
        override val color = Color.Black
    }

    @Parcelize
    object Red: ColorMode() {
        @IgnoredOnParcel
        override val name = "赤"
        @IgnoredOnParcel
        override val color = Color.Red
    }

    abstract val name: String
    abstract val color: Color
}

enum class OperateType {
    Text,
    Rect,
//    Circle
}

enum class Text_Parameter {
    X,
    Y,
    Size,
    Weight,
}

enum class Rect_Parameter {
    X,
    Y,
    WIDTH,
    HEIGHT,
    DEGREE
}

enum class Parameter_Sign {
    Plus,
    Minus
}