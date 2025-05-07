package space.webkombinat.epdc.VIew.CanvasDraw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import space.webkombinat.epdc.Model.CanvasObjects.RectData

fun DrawScope.Canvas_rect(
    item: RectData
) {
    // pivot は描画要素(Rect)の中心
    val pivot = Offset(x = (item.x + (item.size_w/2)).toFloat(), y = (item.y + (item.size_h/2)).toFloat())
    rotate(
        degrees = item.degree.toFloat(),
        pivot = pivot
    ) {
        drawRect(
            color = item.colorMode.color,
            topLeft = Offset(x =item.x.toFloat(), y = item.y.toFloat()),
            size = Size(item.size_w.toFloat(), item.size_h.toFloat())
        )
    }
}