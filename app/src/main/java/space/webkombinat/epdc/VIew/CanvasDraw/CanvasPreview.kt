package space.webkombinat.epdc.VIew.CanvasDraw

import android.R.attr.colorMode
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.ColorMode

fun DrawScope.CanvasPreview(
    list: MutableList<Int>,
    vm: CanvasVM,
    color: Color
) {
    var countX = 0
    var countY = 0
    var rectSize = vm.canvasManager.width_x
    list.forEachIndexed { index, i ->
        if (index % 128 == 0 && index != 0) {
            countY = 0
            countX++
        }
        val cal: Color
        if (i == 1) {
            cal = Color.White.copy(alpha = 0.0f)
        } else {
            cal = color
        }
        drawRect(
            color = cal,
            topLeft = Offset((countX * rectSize).toFloat(), (countY * rectSize).toFloat()),
            size = Size(rectSize.toFloat(), rectSize.toFloat())
        )
        countY++
    }

}

