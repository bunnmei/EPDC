package space.webkombinat.epdc.VIew

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import space.webkombinat.epdc.Model.ColorSet
import space.webkombinat.epdc.ViewModel.CanvasVM

@Composable
fun EDPCanvas(
    modifier: Modifier = Modifier,
    canvas_width: Int,
    canvas_height: Int,
    mask_width: Int,
    top: Boolean,
    captureArea: MutableState<Rect?>? = null,
    indicator: Int? = null,
    draw: DrawScope.() -> Unit
) {
    val data = mask_width
    var canvasSize: Int
    if (top) {
        canvasSize = (data+data+canvas_height)
    } else {
        canvasSize = (data+canvas_height)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(canvasSize.dp)
            .background(Color.White.copy(0.0f))
            .clipToBounds(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
          Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.0f))
                    .height(canvasSize.dp)
          ){
                Canvas(
                    modifier = modifier

                        .width((canvas_width * 3).dp)
                        .height((canvas_height * 3).dp)
                        .offset(x = data.dp, y = data.dp)
                        .background(Color.White.copy(0.0f))
                        .drawWithContent() {
                            draw()
                        }
                ) {

                }

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(canvasSize.dp)
                ) {
                    Row(
                        modifier = modifier
                            .height(data.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ){}
                    Row {
                        Row(
                            modifier = modifier
                                .height(canvas_height.dp)
                                .width(data.dp)
                                .background(
                                    if (indicator == null) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else if (
                                        indicator == 1
                                    ) {
                                        Color.Black
                                    } else {
                                        Color.Red
                                    }
                                )
                        ){}
                        Row (
                            modifier = modifier
                                .height(canvas_height.dp)
                                .width(canvas_width.dp)

                                .onGloballyPositioned { layoutCoordinates ->
                                    val position = layoutCoordinates.positionInWindow()
                                    val size = layoutCoordinates.size.toSize()
//                                    println("posi ${position} - size ${canvasSize}")
//                                    if (captureArea == null) {
//                                        println("captureArea がないよ")
//                                    } else {
//                                        println("captureArea あるよ")
//                                    }
                                    println(" x ${position.x} y = ${position.y} x+w = ${position.x + size.width} y+h = ${position.y + size.height} ")
                                    captureArea?.value = Rect(
                                        position.x.toInt(),
                                        position.y.toInt(),
                                        (position.x + size.width).toInt(),
                                        (position.y + size.height).toInt()
                                    )
                                    println("${captureArea?.value}")
                                }
                        ){}
                        Row(
                            modifier = modifier
                                .height(canvas_height.dp)
                                .width(data.dp)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ){}
                    }
                    if(top) {
                        Row(
                            modifier = modifier
                                .height(data.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ){}
                    }

                }
          }

    }
}