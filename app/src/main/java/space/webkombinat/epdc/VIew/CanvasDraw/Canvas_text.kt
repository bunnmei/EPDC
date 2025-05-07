package space.webkombinat.epdc.VIew.CanvasDraw

import android.content.Context
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
import space.webkombinat.epdc.ViewModel.CanvasVM


fun DrawScope.Canvas_text(
    textMeasurer: TextMeasurer,
    item: TextDate,
    vm: CanvasVM,
    ctx: Context,
) {
//    val font = R.font.heavy_ver8
    drawText(
        text = item.text,
        textMeasurer = textMeasurer,
        style = TextStyle(
            fontSize = item.fontSize.sp,
            fontWeight = FontWeight(item.fontWeight),
            color = item.colorMode.color,
            fontFamily = vm.getFont(ctx = ctx, fontName = item.fontFamily)
//            fontFamily = FontFamily.Monospace
        ),
        topLeft = Offset(
            x = item.x.toFloat(),
            y = item.y.toFloat()
        )
    )
}