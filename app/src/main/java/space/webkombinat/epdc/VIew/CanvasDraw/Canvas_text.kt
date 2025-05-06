package space.webkombinat.epdc.VIew.CanvasDraw

import androidx.compose.runtime.Composable

//import androidx.compose.runtime.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import space.webkombinat.epdc.Model.CanvasObjects.TextDate


fun DrawScope.Canvas_text(
    textMeasurer: TextMeasurer,
    item: TextDate
) {
//    val font = R.font.heavy_ver8
    drawText(
        text = item.text,
        textMeasurer = textMeasurer,
        style = TextStyle(
            fontSize = item.fontSize.sp,
            fontWeight = FontWeight(item.fontWeight),
            color = item.color,
            fontFamily = item.fontFamily
//            fontFamily = FontFamily.Monospace
        ),
        topLeft = Offset(
            x = item.x.toFloat(),
            y = item.y.toFloat()
        )
    )
}