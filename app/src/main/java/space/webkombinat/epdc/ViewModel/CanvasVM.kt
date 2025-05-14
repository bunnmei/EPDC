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
import space.webkombinat.epdc.Model.CanvasObjects.RectData
import space.webkombinat.epdc.Model.Controller.CanvasManager
import space.webkombinat.epdc.Model.CanvasObjects.TextDate
import space.webkombinat.epdc.Model.Controller.FontFolderReader
import space.webkombinat.epdc.Model.Controller.UsbController
import space.webkombinat.epdc.Model.CurrentEditData
import space.webkombinat.epdc.Model.DB.Project.ProjectEntity
import space.webkombinat.epdc.Model.DB.Project.ProjectRepository
import space.webkombinat.epdc.Model.DB.Rect.RectRepository
import space.webkombinat.epdc.Model.DB.Text.TextRepository
import space.webkombinat.epdc.ViewModel.Room_Data
import javax.inject.Inject

@HiltViewModel
class CanvasVM @Inject constructor(
    val canvasManager: CanvasManager,
    val usbController: UsbController,
    val fontFolderReader: FontFolderReader,
    val savedStateHandle: SavedStateHandle,
    val currentEditData: CurrentEditData,
    val projectRepo: ProjectRepository,
    val textRepo: TextRepository,
    val rectRepo: RectRepository,
): ViewModel() {

    private val _uiState = savedStateHandle.getStateFlow("uiStateKey", UiState())
    val uiState: StateFlow<UiState> = _uiState

    var black_previewPixelList = mutableStateListOf<Int>()
    var red_previewPixelList = mutableStateListOf<Int>()
    var usbButtonState = mutableStateOf(true)
    var convertButtonState = mutableStateOf(true)

    @Parcelize
    data class UiState(
        val projectId: Long = -1,
        val itemCounter: Int = 0,
        val selectTab: ColorMode = ColorMode.Black,
        val selectSideList: OperateType = OperateType.Text,
        val operateType: OperateType = OperateType.Text,
        val operateIndex: Int = 0,
        val textItems: List<TextDate> = emptyList(),
        val rectItems: List<RectData> = emptyList(),
        val roomData: Room_Data = Room_Data.NULL
    ) : Parcelable

    init {
        viewModelScope.launch {
            currentEditData.selectedProjectId.collect { it ->
                if (it != null) {
                    println("idがへんこうされました。ListVM")
                    reLoad(id = it)
                } else {
                    val newProject = ProjectEntity(
                        id = 0,
                        projectName = null,
                        createdAt = System.currentTimeMillis()
                    )
                    val projectId = projectRepo.insertProject(newProject)
                    currentEditData.selectProject(projectId)
                    savedStateHandle["uiStateKey"] = UiState()
                }
            }
        }
    }

    suspend fun reLoad(id: Long) {
        val project = projectRepo.getProjectById(id)
        if (project != null) {
            var textItems = mutableListOf<TextDate>()
            var rectItems = mutableListOf<RectData>()
            project.textItems.forEachIndexed { _, textData ->
                textItems.add(textData.toTextDate(num = textData.id))
            }
            project.rectItems.forEachIndexed { _, rectData ->
                rectItems.add(rectData.toRectDate(num = rectData.id))
            }
            val newUiState = UiState().copy(
                projectId = id,
                rectItems = rectItems,
                textItems = textItems,
                operateIndex = textItems.size,
                roomData = Room_Data.OK
            )
            savedStateHandle["uiStateKey"] = newUiState
        }
    }

    fun fontReload(ctx: Context) {
        fontFolderReader.createFontFolder(ctx = ctx)
    }

    fun set_openList(mode: OperateType) {
        val newData = uiState.value.copy(selectSideList = mode)
        savedStateHandle["uiStateKey"] = newData
    }

    fun setTabMode(mode: ColorMode) {
        val newData = _uiState.value.copy(selectTab = mode)
        savedStateHandle["uiStateKey"] = newData
    }

    fun getScreenSize(ctx: Context): Triple<Int, Int, Int> {
        val canvasWidth = pxToDp(canvasManager.virual_EPD_W, ctx)
        val canvasHeight = pxToDp(canvasManager.virual_EPD_H, ctx)
        val maskSize = pxToDp(
            canvasManager.screenWidth - canvasManager.virual_EPD_W,
            ctx = ctx
        ) / 2
        return Triple(canvasWidth, canvasHeight, maskSize)
    }

    fun usbConnectionSetup(ctx: Context) {
        usbController.getDevice(ctx = ctx)
    }

    fun usbDataTransfer() {
        if (usbButtonState.value) {
            usbButtonState.value = false
            val th = Thread {
                if (black_previewPixelList.isNotEmpty() && red_previewPixelList.isNotEmpty()) {
                    val transferDataByte =
                        canvasManager.listIntToByteArrayDirect(black_previewPixelList)

                    val convertZeroToOne = canvasManager.redZeroToOne(red_previewPixelList)
                    val transferDataByte_red =
                        canvasManager.listIntToByteArrayDirect(convertZeroToOne)

                    println("re array size ${transferDataByte_red.size}${transferDataByte[0]}")
                    println("bl array size ${transferDataByte.size}${transferDataByte[0]}")
                    if (transferDataByte.size == 4736 && transferDataByte_red.size == 4736) {
                        try {
                            usbController.transferData(transferDataByte)
                            Thread.sleep(100) // pi pico のバッファが溢れないように
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
        when (_uiState.value.selectTab) {
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
        val newData = _uiState.value.copy(operateType = mode)
        savedStateHandle["uiStateKey"] = newData
    }

    fun setOperateIndex(index: Int) {
        val newData = _uiState.value.copy(operateIndex = index)
        savedStateHandle["uiStateKey"] = newData
    }

    fun change(num: Long, operateType: OperateType) {
        setOperateType(mode = operateType)
        when (operateType) {
            OperateType.Text -> {
                val i = uiState.value.textItems.indexOfFirst { it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    setOperateIndex(index = i + 1)
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }

            OperateType.Rect -> {
                val i = uiState.value.rectItems.indexOfFirst { it.id == num }
                println("i == ${i} , ${num}")
                if (i >= 0) {
                    setOperateIndex(index = i + 1)
                } else {
                    println("存在しない要素がおされたよ ${num}")
                }
            }
        }
        println("change id ${num}")

    }

    fun addText() {
        var newText = TextDate(
            id = 0,
            text = "dammy",
            x = 0,
            y = 0,
            fontSize = 50,
            fontWeight = 300,
            fontFamily = FontFamily.Default.toString(),
            colorMode = uiState.value.selectTab
        )

        viewModelScope.launch {
            val data =
                textRepo.insertText(newText.toTextEntity(projectId = currentEditData.selectedProjectId.value!!))
            println("data id ${data}")
            val textReturnRoom = newText.copy(id = data)
            println("data ${textReturnRoom.text}")
            savedStateHandle["uiStateKey"] = uiState.value.copy(
                textItems = uiState.value.textItems + textReturnRoom,
                operateType = OperateType.Text,
                operateIndex = uiState.value.textItems.size + 1,
            )
        }
    }

    fun updateText(newText: String) {
        val currentData = uiState.value.textItems[uiState.value.operateIndex - 1]
        val newData = currentData.copy(text = newText)
        val updateTextItems =
            uiState.value.textItems.map { if (it.id == currentData.id) newData else it }
        viewModelScope.launch {
            textRepo.updateText(
                newData.toTextEntity(
                    itemId = newData.id,
                    projectId = uiState.value.projectId
                )
            )
        }
        savedStateHandle["uiStateKey"] =
            uiState.value.copy(textItems = updateTextItems, roomData = Room_Data.UNSAVED)
    }

    fun removeText(targetId: Long) {
        val updatedTextItems = uiState.value.textItems.filter { it.id != targetId }
        if (targetId >= 0) {
            viewModelScope.launch {
                val deleteText = uiState.value.textItems.find { it.id == targetId }
                if (deleteText != null) {
                    textRepo.deleteText(
                        deleteText.toTextEntity(
                            itemId = deleteText.id,
                            projectId = uiState.value.projectId
                        )
                    )
                }
            }
        }
        val length = updatedTextItems.size - 1
        if (length > 0) {
            setOperateIndex(length)
        } else {
            setOperateIndex(1)
        }
        setOperateType(OperateType.Text)
        val newUiState =
            if (uiState.value.textItems.size == 0 || uiState.value.rectItems.size == 0) {
                uiState.value.copy(textItems = updatedTextItems, roomData = Room_Data.NULL)
            } else {
                uiState.value.copy(textItems = updatedTextItems)
            }
        savedStateHandle["uiStateKey"] = newUiState
    }

    fun addRect() {
        val newRect = RectData(
            id = 0,
            x = 0,
            y = 0,
            size_h = 50,
            size_w = 50,
            degree = 0,
            colorMode = uiState.value.selectTab
        )
        viewModelScope.launch {
            val data =
                rectRepo.insertRect(newRect.toRectEntity(projectId = currentEditData.selectedProjectId.value!!))
            val rectReturnRoom = newRect.copy(id = data)
            savedStateHandle["uiStateKey"] = uiState.value.copy(
                rectItems = uiState.value.rectItems + rectReturnRoom,
                operateType = OperateType.Rect,
                operateIndex = uiState.value.rectItems.size + 1,
            )
        }
    }

    fun removeRect(targetId: Long) {
        val updatedRectItems = uiState.value.rectItems.filter { it.id != targetId }
        if (targetId >= 0) {
            viewModelScope.launch {
                val deleteRect = uiState.value.rectItems.find { it.id == targetId }
                if (deleteRect != null) {
                    rectRepo.deleteRect(
                        deleteRect.toRectEntity(
                            itemId = deleteRect.id,
                            projectId = uiState.value.projectId
                        )
                    )
                }
            }
        }
        val length = updatedRectItems.size - 1
        if (length > 0) {
            setOperateIndex(length)
        } else {
            setOperateIndex(1)
        }
        setOperateType(OperateType.Rect)
        val newUiState =
            if (uiState.value.textItems.size == 0 || uiState.value.rectItems.size == 0) {
                uiState.value.copy(rectItems = updatedRectItems, roomData = Room_Data.NULL)
            } else {
                uiState.value.copy(rectItems = updatedRectItems)
            }
        savedStateHandle["uiStateKey"] = newUiState
    }

    fun rectParameterUpdate(
        updateParameter: Rect_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = uiState.value.rectItems[uiState.value.operateIndex - 1]
        val updateFunction: (Int) -> Int = when (sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrementRect(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrementRect(updateParameter) }
        }
        val newData = when (updateParameter) {
            Rect_Parameter.X -> currentData.copy(x = updateFunction(currentData.x))
            Rect_Parameter.Y -> currentData.copy(y = updateFunction(currentData.y))
            Rect_Parameter.WIDTH -> currentData.copy(size_w = updateFunction(currentData.size_w))
            Rect_Parameter.HEIGHT -> currentData.copy(size_h = updateFunction(currentData.size_h))
            Rect_Parameter.DEGREE -> currentData.copy(degree = updateFunction(currentData.degree))
        }
        val updateRectItems =
            uiState.value.rectItems.map { if (it.id == currentData.id) newData else it }
        viewModelScope.launch {
            rectRepo.updateRect(
                newData.toRectEntity(
                    itemId = newData.id,
                    projectId = uiState.value.projectId
                )
            )
        }
        savedStateHandle["uiStateKey"] =
            uiState.value.copy(rectItems = updateRectItems, roomData = Room_Data.UNSAVED)
    }

    fun textParameterUpdate(
        updateParameter: Text_Parameter,
        sign: Parameter_Sign
    ) {
        val currentData = uiState.value.textItems[uiState.value.operateIndex - 1]
        val updateFunction: (Int) -> Int = when (sign) {
            Parameter_Sign.Plus -> { value -> value + getIncrementText(updateParameter) }
            Parameter_Sign.Minus -> { value -> value - getIncrementText(updateParameter) }
        }
        val newData = when (updateParameter) {
            Text_Parameter.Weight -> {
                val newValue = updateFunction(currentData.fontWeight)
                if (newValue in 200..1000) { // Weight範囲チェック
                    currentData.copy(fontWeight = newValue)
                } else {
                    return
                }
            }

            Text_Parameter.Size -> currentData.copy(fontSize = updateFunction(currentData.fontSize))
            Text_Parameter.X -> currentData.copy(x = updateFunction(currentData.x))
            Text_Parameter.Y -> currentData.copy(y = updateFunction(currentData.y))
        }

        val updateTextItems =
            uiState.value.textItems.map { if (it.id == currentData.id) newData else it }
        viewModelScope.launch {
            textRepo.updateText(
                newData.toTextEntity(
                    itemId = newData.id,
                    projectId = uiState.value.projectId
                )
            )
        }
        savedStateHandle["uiStateKey"] =
            uiState.value.copy(textItems = updateTextItems, roomData = Room_Data.UNSAVED)
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


    fun getFont(ctx: Context, fontName: String): FontFamily {
        when (fontName) {
            FontFamily.Default.toString() -> return FontFamily.Default
            FontFamily.Monospace.toString() -> return FontFamily.Monospace
            FontFamily.Serif.toString() -> return FontFamily.Serif
            FontFamily.SansSerif.toString() -> return FontFamily.SansSerif
            else -> {
                return fontFolderReader.getFontFamily(ctx = ctx, fileName = fontName)
            }
        }
    }

    fun checkFont(fontName: String): Boolean {
        return when (fontName) {
            FontFamily.Default.toString() -> true
            FontFamily.Monospace.toString() -> true
            FontFamily.Serif.toString() -> true
            FontFamily.SansSerif.toString() -> true
            else -> {
                false
            }
        }
    }

    fun changeFont(ctx: Context, fontName: String) {
        var font: String
        if (checkFont(fontName)) {
            font = getFont(ctx, fontName).toString()
        } else {
            font = fontName
        }
        val currentData = uiState.value.textItems[uiState.value.operateIndex - 1]
        val newData = currentData.copy(fontFamily = font)
        savedStateHandle["uiStateKey"] = uiState.value.copy(
            textItems = uiState.value.textItems
                .map { if (it.id == currentData.id) newData else it })
    }

    private fun checkAlreadyProject(): Boolean {
        if (uiState.value.projectId >= 0) {
            return true
        }
        return false
    }
//    fun ok() {
//        currentEditData.selectProject(uiState.value.projectId)
//        viewModelScope.launch {
//            reLoad(id = currentEditData.selectedProjectId.value!!)
//            savedStateHandle["uiStateKey"] = uiState.value.copy(roomData = Room_Data.OK)
//        }
//    }

    fun pxToDp(size: Int, ctx: Context): Int {
        val density = ctx.resources.displayMetrics.density
        return (size / density).toInt()
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

enum class Room_Data {
    NULL,
    UNSAVED,
    UPDATING,
    UPDATED,
    SAVING,
    SAVED,
    OK,
}

enum class Parameter_Sign {
    Plus,
    Minus
}