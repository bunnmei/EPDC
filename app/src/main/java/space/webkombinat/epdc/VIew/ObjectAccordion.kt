package space.webkombinat.epdc.VIew

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.OperateType

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ObjectAccordion(
    modifier: Modifier = Modifier,
    vm: CanvasVM,
    click: (Int, OperateType, Color) -> Unit,
) {
   val openList by vm.uiState.collectAsState()

    BoxWithConstraints(modifier = modifier.background(Color.White)) {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val targetWidthPx = maxWidthPx - with(LocalDensity.current) { (50*OperateType.entries.size).dp.toPx() }
        val targetWidthDp = with(LocalDensity.current) { targetWidthPx.toDp() }

        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            OperateType.entries.forEach { type ->
                Row (
                    modifier.fillMaxHeight()
                        .width(50.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            vm.set_openList(type)
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {
                        Spacer(modifier = modifier.height(8.dp))
                        when (type) {
                            OperateType.Text -> {
                                Text("${type.name}")
                            }
                            OperateType.Rect -> {
                                Spacer(modifier = modifier.height(8.dp))
                                Box(modifier = modifier.width(16.dp).height(16.dp).background(Color.Black))
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = openList.selectSideList == type,
                    enter = expandHorizontally(
                        animationSpec = tween(durationMillis = 150)
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(durationMillis = 150)
                    )
                ) {
                    LazyColumn(
                        modifier = modifier.width(targetWidthDp)
                            .background(Color.White)
                    ) {
                        if (type == OperateType.Text) {
                            itemsIndexed(vm.text_items) { index,item ->
                                Row(
                                    modifier = modifier.fillMaxWidth()
                                        .height(60.dp)
                                        .combinedClickable(
                                            onClick = {
                                                click(item.id, type, item.color)
                                            },
                                            onLongClick = {
                                                vm.remove_text(item.id)
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "T",
                                        fontFamily = FontFamily.Serif,
                                        color = item.color
                                        )
                                    Spacer(modifier = modifier.width(8.dp))
                                    Text("${item.text} - ${item.id}")
                                }
                            }
                        }
                        if (type == OperateType.Rect) {
                            itemsIndexed(vm.rect_items) { index,item ->
                                Row(
                                    modifier = modifier.fillMaxWidth()
                                        .height(60.dp)
                                        .combinedClickable(
                                            onClick = {
                                                click(item.id, type, item.color)
                                            },
                                            onLongClick = {
                                                vm.remove_rect(item.id)
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = modifier.height(16.dp)
                                            .width(16.dp)
                                            .background(item.color)
                                    )
                                    Spacer(modifier = modifier.width(8.dp))
                                    Text("Rect - ${item.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}