package space.webkombinat.epdc.Model.DB.Text

import space.webkombinat.epdc.Model.CanvasObjects.TextDate
import space.webkombinat.epdc.Model.Constants

data class Text(
    val id: Long,
    val projectId: Long,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var fontFamily: String,
    var colorMode: Int
)