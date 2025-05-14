package space.webkombinat.epdc.VIew.Screen

import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.drawToBitmap
import space.webkombinat.epdc.VIew.BottomFloatingButton
import space.webkombinat.epdc.VIew.CanvasDraw.CanvasPreview
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_rect
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_text
import space.webkombinat.epdc.VIew.CanvasTab
import space.webkombinat.epdc.VIew.EDPCanvas
import space.webkombinat.epdc.VIew.OperateBottomSheet
import space.webkombinat.epdc.VIew.OperateButtons
import space.webkombinat.epdc.VIew.RectDataEditor
import space.webkombinat.epdc.VIew.SideObjectList
import space.webkombinat.epdc.VIew.TextDataEditor
import space.webkombinat.epdc.VIew.TopAppBar
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.ColorMode
import space.webkombinat.epdc.ViewModel.OperateType

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    vm: CanvasVM
) {
    val view = LocalView.current
    val ctx = LocalContext.current
    val (canvasWidth, canvasHeight, maskSize) = vm.getScreenSize(ctx = ctx)
    val tabList = listOf(
        ColorMode.Black,
        ColorMode.Red,
    )
    val textMeasurer = rememberTextMeasurer()
    val textMeasurer2 =  rememberTextMeasurer()
    val captureArea = remember { mutableStateOf<Rect?>(null) }

    val addOpenDialog = remember { mutableStateOf(false) }
    val drawerState = remember { mutableStateOf(false) }
    var showBottomSheet = remember { mutableStateOf(false) }
    val uiState by vm.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan.copy(0.0f))
    ) {
        Column {
            TopAppBar(
                click = {
                    drawerState.value = !drawerState.value
                },
                click2 = {
                    showBottomSheet.value = true
                    drawerState.value = false
                },
                vm = vm
            )
            SideObjectList(
                drawerState = drawerState,
                click = {
                    showBottomSheet.value = true
                    drawerState.value = false
                },
                vm = vm,
            ) {
                CanvasTab(
                    tabList = tabList,
                    vm = vm
                )
//                                  Edit Canvas Screen
//                                        val font = FontFamily(Font(R.font.heaby))
                EDPCanvas(
                    canvas_width = canvasWidth,
                    canvas_height = canvasHeight,
                    mask_width = maskSize,
                    top = false,
                    captureArea = captureArea,
                    indicator = true,
                    vm = vm
                ) {
//                                        drawCircle(
//                                            color = Color.Red,
//                                            radius = 200f,
//                                            center = Offset(x = 0f, y = 0f),
//                                            style = Stroke(width = 5f)
//                                        )
                    drawRect(
                        color = Color.White
                    )
                    uiState.textItems.forEach { item ->
                        if (uiState.selectTab == item.colorMode) {
                            when (item.colorMode) {
                                ColorMode.Black -> {
                                    Canvas_text(
                                        textMeasurer = textMeasurer,
                                        item = item,
                                        vm = vm,
                                        ctx = ctx
                                    )
                                }

                                ColorMode.Red -> {
                                    Canvas_text(
                                        textMeasurer = textMeasurer2,
                                        item = item,
                                        vm = vm,
                                        ctx = ctx
                                    )
                                }
                            }
                        }
                    }

                    uiState.rectItems.forEach { item ->
                        if (uiState.selectTab == item.colorMode) {
                            when (item.colorMode) {
                                ColorMode.Black -> {
                                    Canvas_rect(
                                        item = item
                                    )
                                }

                                ColorMode.Red -> {
                                    Canvas_rect(
                                        item = item
                                    )
                                }
                            }
                        }
                    }
                }
                // 296 * 128 EDPPreview
                EDPCanvas(
                    canvas_width = canvasWidth,
                    canvas_height = canvasHeight,
                    mask_width = maskSize,
                    top = true,
                    vm = vm
                ) {
                    drawRect(
                        color = Color.White
                    )
                    tabList.forEach { item ->
                        when (item) {
                            ColorMode.Black -> {
                                CanvasPreview(
                                    list = vm.black_previewPixelList,
                                    vm = vm,
                                    color = item.color
                                )
                            }

                            ColorMode.Red -> {
                                CanvasPreview(
                                    list = vm.red_previewPixelList,
                                    vm = vm,
                                    color = item.color
                                )
                            }
                        }
                    }
                }

                OperateButtons(
                    vm = vm,
                ) {
                    captureArea.value?.let { rect ->
                        vm.convert(
                            rect = rect,
                            bitmap = view.drawToBitmap(),
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                BottomFloatingButton {
                    addOpenDialog.value = true
                }
            }
        }

        if (addOpenDialog.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {
                        addOpenDialog.value = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(fraction = 0.8f)
                        .background(Color.White)
                        .clickable {
                            println("Dialogが押されたよ")
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.White)
                            .clickable {
                                addOpenDialog.value = false
                                vm.addText()
                                showBottomSheet.value = true
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Text",
                            fontSize = 20.sp
                        )
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.White)
                            .clickable {
                                addOpenDialog.value = false
                                vm.addRect()
                                showBottomSheet.value = true
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rect",
                            fontSize = 20.sp
                        )
                    }
                    HorizontalDivider()
                }
            }
        }

        OperateBottomSheet(
            showBottomSheet = showBottomSheet
        ) {
//                                    val mode = canvasVM.operate_data_type.value
            if (uiState.textItems.isNotEmpty() && uiState.operateType == OperateType.Text) {
                TextDataEditor(
                    vm = vm
                )
            }
            if (uiState.rectItems.isNotEmpty() && uiState.operateType == OperateType.Rect) {
                RectDataEditor(
                    vm = vm
                )
            }
        }

    }
}