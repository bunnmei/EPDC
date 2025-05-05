package space.webkombinat.epdc.VIew

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Alingment
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import space.webkombinat.epdc.ViewModel.CanvasVM

@Composable
fun SideObjectList(
    modifier: Modifier = Modifier,
    drawerState: MutableState<Boolean>,
    vm: CanvasVM,
    click: () -> Unit,
    content: @Composable (() -> Unit),
) {
//    val drawerState = remember { mutableStateOf(false) }

    Box {
        // Main Content
        Column {
            content()
        }

        if (
            drawerState.value
        ){
            Column (
                modifier = modifier.fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {
                        drawerState.value = false
                    }
            ) {  }
        }


        AnimatedVisibility(
//            modifier = modifier.fillMaxSize(fraction = 0.8f),
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = drawerState.value,
            enter = slideInHorizontally(initialOffsetX = { it}),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd), // 右端に配置
//                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 16.dp
            ) {
                ObjectAccordion()
//                LazyColumn {
//                    itemsIndexed(vm.text_items) { index,item ->
//                        Row(
//                            modifier = modifier.fillMaxWidth()
//                                .height(60.dp)
//                                .combinedClickable(
//                                    onClick = {
//                                        drawerState.value = false
//                                        vm.change(item.id)
//                                        click()
//                                    },
//                                    onLongClick = {
//                                        vm.remove_text(item.id)
//                                    }
//                                ),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Box(
//                                modifier = modifier.height(16.dp)
//                                    .width(16.dp)
//                                    .background(item.color)
//                            )
//                            Spacer(modifier = modifier.width(8.dp))
//                            Text("${item.text} - ${item.id}")
//                        }
//                    }
//                }

            }
        }

        // Right-side Drawer
//        AnimatedVisibility(
//            visible = drawerState.value,
//            enter = slideInHorizontally(initialOffsetX = { it }),
//            exit = slideOutHorizontally(targetOffsetX = { it })
//        ) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(300.dp)
//                    .align(Alignment.CenterEnd), // 右端に配置
//                color = MaterialTheme.colorScheme.surface,
//                tonalElevation = 16.dp
//            ) {
//               Text("sidemenu")
//            }
//        }
    }
}

