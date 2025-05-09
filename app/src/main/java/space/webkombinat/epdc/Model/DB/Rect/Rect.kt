package space.webkombinat.epdc.Model.DB.Rect


data class Rect (
    val id: Long,
    val projectId: Long,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var colorMode: Int
)
