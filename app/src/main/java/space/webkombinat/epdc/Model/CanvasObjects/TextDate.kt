package space.webkombinat.epdc.Model.CanvasObjects

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import space.webkombinat.epdc.ViewModel.ColorMode

data class TextDate(
    val id: Int,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var fontFamily: FontFamily,
    var color: Color,
    var colorMode: ColorMode
)