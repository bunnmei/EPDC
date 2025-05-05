package space.webkombinat.epdc.Model.CanvasObjects

import androidx.compose.ui.graphics.Color

data class RectData(
    val id: Int,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var color: Color,
    var colorId: Int
)