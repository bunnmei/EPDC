package space.webkombinat.epdc.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import space.webkombinat.epdc.Model.CanvasObjects.RectData
import space.webkombinat.epdc.Model.Controller.CanvasManager
import space.webkombinat.epdc.Model.CanvasObjects.TextDate
import space.webkombinat.epdc.Model.ColorSet
import space.webkombinat.epdc.Model.Controller.UsbController
import javax.inject.Inject

@HiltViewModel
class CanvasVM @Inject constructor(
    val canvasManager: CanvasManager,
    val usbController: UsbController
): ViewModel() {
    var item_count by mutableStateOf(0)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    var operate_data_id = mutableStateOf(0)
    var operate_data_type = mutableStateOf<OperateType>(OperateType.Text)

    var text_items = mutableStateListOf<TextDate>()
    var rect_items = mutableStateListOf<RectData>()

    var black_previewPixelList =  mutableStateListOf<Int>()
    var red_previewPixelList = mutableStateListOf<Int>()

    data class UiState(
        val selectTab: ColorMode = ColorMode.Black
    )

    fun printHiltVM(){
        println("HiltでinjectされたVMの関数が呼ばれたよ")
        canvasManager.printScreenSize()
    }

    fun setTabMode(mode: ColorMode) {
        _uiState.value = _uiState.value.copy(selectTab = mode)
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
        viewModelScope.launch {
            if (black_previewPixelList.isNotEmpty()) {
                val transferDataByte = canvasManager.listIntToByteArrayDirect(black_previewPixelList)
                usbController.transferData(transferDataByte)
                val convertZeroToOne = canvasManager.redZeroToOne(red_previewPixelList)
                val transferDataByte_red = canvasManager.listIntToByteArrayDirect(convertZeroToOne)
                usbController.transferData(transferDataByte_red)
            }
        }
    }

    fun convert_r(bitmap: Bitmap, rect: Rect): Bitmap {
        println("Red convet")
        val newBitmap = Bitmap.createBitmap(
            bitmap,
            rect.left,
            rect.top,
            rect.width(),
            rect.height()
        )
        println("newBitmap width = ${newBitmap.width} height = ${newBitmap.height}")
        red_previewPixelList.clear()
        viewModelScope.launch {
            val hoge = canvasManager.bitmapToHexList(bitmap = newBitmap)
            println("hoge length ${hoge.size}")
            red_previewPixelList.addAll(hoge)
        }
        return newBitmap
    }

    fun convert(bitmap: Bitmap, rect: Rect) {
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
//                    println("hoge length ${hoge.size}")
                    black_previewPixelList.addAll(newList)
                }
            }
            ColorMode.Red -> {
                red_previewPixelList.clear()
                viewModelScope.launch {
                    val newList = canvasManager.bitmapToHexList(bitmap = newBitmap)
//                    println("hoge length ${hoge.size}")
                    red_previewPixelList.addAll(newList)
                }
            }
        }

    }

    fun change(num: Int, operateType: OperateType) {
        operate_data_type.value = operateType
        when(operateType){
            OperateType.Text -> {
                val i = text_items.indexOfFirst{ it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    operate_data_id.value = i+1
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }
            OperateType.Rect -> {
                val i = rect_items.indexOfFirst{ it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    operate_data_id.value = i+1
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }
//            OperateType.Circle -> {}
        }
        println("change id ${num}")

    }
    fun remove_text(selectId: Int) {
        val fined = text_items.removeIf { item -> item.id == selectId }
        if (fined) {
          operate_data_id.value = text_items.size - 1
            operate_data_type.value = OperateType.Text
        } else {
            println("見つからなかったよ ${selectId}")
        }
    }
    fun add_text() {
        item_count++

        var newText = TextDate(
            id = item_count,
            text = "dammy",
            x = 0,
            y = 0,
            fontSize = 50,
            color = _uiState.value.selectTab.color,
            fontWeight = 300,
            colorMode = _uiState.value.selectTab
        )
        text_items.add(newText)
        operate_data_type.value = OperateType.Text
//        println("last = ${text_items.lastIndex}")
        operate_data_id.value = text_items.size
    }

    fun remove_rect(selectId: Int) {
        val fined = rect_items.removeIf { item -> item.id == selectId }
        if (fined) {
            operate_data_id.value = rect_items.size - 1
            operate_data_type.value = OperateType.Rect
        } else {
            println("見つからなかったよ ${selectId}")
        }
    }
    fun add_rect() {
        item_count++
        val newRect = RectData(
            id = item_count,
            x = 0,
            y = 0,
            size_h = 50,
            size_w = 50,
            degree = 0,
            color = _uiState.value.selectTab.color,
            colorMode = _uiState.value.selectTab
        )
        rect_items.add(newRect)
        operate_data_type.value = OperateType.Rect
        operate_data_id.value = rect_items.size
    }

    fun changeText(newText: String) {
        val currentData = text_items[operate_data_id.value - 1]
        val newData = currentData.copy(text = newText)
        text_items[operate_data_id.value - 1] = newData
    }

    fun rect_parameter_update(
        updateParameter: Rect_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = rect_items[operate_data_id.value - 1]
        val updateFunction: (Int) -> Int = when(sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrement_rect(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrement_rect(updateParameter) }
        }
        val newData = when (updateParameter) {
            Rect_Parameter.X -> currentData.copy(x = updateFunction(currentData.x))
            Rect_Parameter.Y -> currentData.copy(y = updateFunction(currentData.y))
            Rect_Parameter.WIDTH -> currentData.copy(size_w = updateFunction(currentData.size_w))
            Rect_Parameter.HEIGHT ->  currentData.copy(size_h = updateFunction(currentData.size_h))
            Rect_Parameter.DEGREE -> currentData.copy(degree = updateFunction(currentData.degree))
        }
        rect_items[operate_data_id.value - 1] = newData
    }

    fun text_parameter_update(
        updateParameter: Text_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = text_items[operate_data_id.value - 1]
        val updateFunction: (Int) -> Int = when (sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrement_text(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrement_text(updateParameter) }
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
        text_items[operate_data_id.value - 1] = newData
    }

    private fun getIncrement_text(parameter: Text_Parameter): Int = when (parameter) {
        Text_Parameter.Weight -> 100
        Text_Parameter.Size -> 10
        Text_Parameter.X -> 10
        Text_Parameter.Y -> 10
    }

    private fun getIncrement_rect(parameter: Rect_Parameter): Int = when (parameter) {
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
}

sealed class ColorMode (
    val name: String,
    val color: Color,
) {
    object Black: ColorMode(name = "黒", color = Color.Black)
    object Red: ColorMode(name= "赤", color = Color.Red)
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