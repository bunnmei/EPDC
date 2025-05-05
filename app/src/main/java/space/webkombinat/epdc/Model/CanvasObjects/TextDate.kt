package space.webkombinat.epdc.Model.CanvasObjects

import androidx.compose.ui.graphics.Color

data class TextDate(
    val id: Int,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var color: Color,
    var colorId: Int
)