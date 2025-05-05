package space.webkombinat.epdc

import android.R
import android.content.res.Resources
import android.graphics.Rect
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.drawToBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import space.webkombinat.epdc.Model.BottomNavigation
import space.webkombinat.epdc.Model.ColorSet
import space.webkombinat.epdc.Model.Controller.ACTION_USB_PERMISSION
import space.webkombinat.epdc.VIew.BottomNavigationBar
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_rect
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_text
import space.webkombinat.epdc.VIew.CanvasTab
import space.webkombinat.epdc.VIew.EDPCanvas
import space.webkombinat.epdc.VIew.OperateBottomSheet
import space.webkombinat.epdc.VIew.Receiver.SystemBroadcastReceiver
import space.webkombinat.epdc.VIew.RectDataEditor
import space.webkombinat.epdc.VIew.SideObjectList
import space.webkombinat.epdc.VIew.TextDataEditor
import space.webkombinat.epdc.VIew.TopAppBar
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.OperateType
import space.webkombinat.epdc.ui.theme.EPDCTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EPDCTheme {
                val view = LocalView.current
                val ctx = LocalContext.current
                val textMeasurer = rememberTextMeasurer()
                val textMeasurer2 =  rememberTextMeasurer()
                val captureArea = remember { mutableStateOf<Rect?>(null) }
                val captureArea_r = remember { mutableStateOf<Rect?>(null) }
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val selectTab = remember { mutableStateOf(1) }
                val addOpenDialog = remember { mutableStateOf(false) }
                val drawerState = remember { mutableStateOf(false) }
                var showBottomSheet = remember { mutableStateOf(false) }
                var prevbitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController
                        ){ bottom ->
                            navController.navigate(
                                route = bottom.route,
                            ) {
                                println("build navig ${backStackEntry.value?.destination?.route}")
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                ) { innerPadding ->
                    val canvasVM = hiltViewModel<CanvasVM>()
                    val canvasWidth = canvasVM.pxToDp(canvasVM.canvasManager.virual_EPD_W, ctx)
                    val canvasHeight = canvasVM.pxToDp(canvasVM.canvasManager.virual_EPD_H, ctx)
                    val maskSize = canvasVM.pxToDp(
                        canvasVM.canvasManager.screenWidth - canvasVM.canvasManager.virual_EPD_W,
                        ctx = ctx
                    ) / 2
                    val tabList = listOf(
                        ColorSet(colorName = "黒", color = Color.Black, id = 1),
                        ColorSet(colorName = "赤", color = Color.Red, id = 2)
                    )
                    SystemBroadcastReceiver { intent ->
                        if (intent?.action == ACTION_USB_PERMISSION) {
                            val manager = ctx.getSystemService(USB_SERVICE) as? UsbManager
                            if (manager!!.deviceList.isNotEmpty()) {
                                val device = manager.deviceList?.values?.first()
                                val hasPermission = manager.hasPermission(device)
                                if (hasPermission) {
                                    canvasVM.usbCommunicationSetup()
                                }
                            }
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavigation.Canvas.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = BottomNavigation.Canvas.route
                        ){

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Cyan.copy(0.0f))
                            ) {
                                Column {
                                    TopAppBar {drawerState.value = !drawerState.value}
                                    SideObjectList(
                                        drawerState = drawerState,
                                        click = {showBottomSheet.value = true},
                                        vm = canvasVM
                                    ){
                                    CanvasTab(
                                        tabList = tabList,
                                        selectedTabIndex = selectTab,
                                    )
//                                  Edit Canvas Screen
                                    EDPCanvas(
                                        canvas_width = canvasWidth,
                                        canvas_height = canvasHeight,
                                        mask_width = maskSize,
                                        top = false,
                                        captureArea = captureArea,
                                        indicator = selectTab.value,
                                    ) {
                                        canvasVM.text_items.forEach { item ->
                                            if (item.colorId == 1 && selectTab.value == 1) {
                                                Canvas_text(
                                                    textMeasurer = textMeasurer,
                                                    item = item
                                                )
                                            } else if (item.colorId == 2 && selectTab.value == 2) {
                                                Canvas_text(
                                                    textMeasurer = textMeasurer2,
                                                    item = item
                                                )
                                            }
                                        }

                                        canvasVM.rect_items.forEach { item ->
                                            if (item.colorId == 1 && selectTab.value == 1) {
                                                Canvas_rect(
                                                    item = item
                                                )
                                            } else if (item.colorId == 2 && selectTab.value == 2) {
                                                Canvas_rect(
                                                    item = item
                                                )
                                            }
                                        }
                                    }
                                    // 296 * 128 EDPPreview
                                    EDPCanvas(
                                        canvas_width = canvasWidth,
                                        canvas_height = canvasHeight,
                                        mask_width = maskSize,
                                        top = true
                                    ) {
                                        var countX = 0
                                        var countY = 0
                                        var rectSize = canvasVM.canvasManager.width_x
                                        canvasVM.black_previewPixelList.forEachIndexed { index, i ->
                                            if(index % 128 == 0 && index != 0){
                                                countY = 0
                                                countX++
                                            }
                                            val cal: Color
                                            if(i == 1) {
                                                cal = Color.White.copy(alpha = 0.0f)
                                            }else {
                                                cal = Color.Black
                                            }
                                            drawRect(
                                                color = cal,
                                                topLeft = Offset((countX*rectSize).toFloat(),(countY*rectSize).toFloat()),
                                                size = Size(rectSize.toFloat(), rectSize.toFloat()))
                                            countY++
                                        }
                                        countX = 0 //reset
                                        countY = 0
                                        canvasVM.red_previewPixelList.forEachIndexed { index, i ->
                                            if(index % 128 == 0 && index != 0){
                                                countY = 0
                                                countX++
                                            }
                                            val cal: Color
                                            if(i == 1) {
                                                cal = Color.White.copy(alpha = 0.0f)
                                            }else {
                                                cal = Color.Red
                                            }
                                            drawRect(
                                                color = cal,
                                                topLeft = Offset((countX*rectSize).toFloat(),(countY*rectSize).toFloat()),
                                                size = Size(rectSize.toFloat(), rectSize.toFloat()))
                                            countY++
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.height(66.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                if (selectTab.value == 1) {
                                                    captureArea.value?.let { rect ->
                                                        canvasVM.convert(rect = rect, bitmap =view.drawToBitmap())
                                                    }
                                                } else if (selectTab.value == 2) {
                                                    captureArea.value?.let { rect ->
                                                        canvasVM.convert_r(rect = rect, bitmap =view.drawToBitmap())
                                                    }
                                                }
                                            },
                                            contentPadding = PaddingValues(0.dp),
                                            modifier = Modifier
                                                .width(50.dp)
                                                .height(50.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Scanner,
                                                contentDescription = "convert canvas to bitmap"
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))
                                        if(canvasVM.usbController.connected.value == false) {
                                            Button(
                                                onClick = {
                                                    canvasVM.usbConnectionSetup(ctx = ctx)
                                                },
                                                contentPadding = PaddingValues(0.dp),
                                                modifier = Modifier
                                                    .width(50.dp)
                                                    .height(50.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.UsbOff,
                                                    contentDescription = "usb setup"
                                                )
                                            }
                                        } else {
                                            Button(
                                                onClick = {
                                                    canvasVM.usbDataTransfer()
                                                },
                                                contentPadding = PaddingValues(0.dp),
                                                modifier = Modifier
                                                    .width(50.dp)
                                                    .height(50.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Usb,
                                                    contentDescription = "usb setup"
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Spacer(modifier = Modifier.weight(1f))
                                        FloatingActionButton(
                                            onClick = {
                                                addOpenDialog.value = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "add object"
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                                if (addOpenDialog.value) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Gray.copy(alpha = 0.8f))
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
                                                        println("selctTab Color = ${selectTab.value}")
                                                        canvasVM.add_text(selectTab = selectTab.value)
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
                                                        println("selctTab Color = ${selectTab.value}")
                                                        canvasVM.add_rect(selectTab = selectTab.value)
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
                                    val mode = canvasVM.operate_data_type.value
                                    if (canvasVM.text_items.isNotEmpty() && mode == OperateType.Text) {
                                        TextDataEditor(
                                            vm = canvasVM
                                        )
                                    }
                                    if (canvasVM.rect_items.isNotEmpty() && mode == OperateType.Rect) {
                                        RectDataEditor(
                                            vm = canvasVM
                                        )
                                    }

                                }

                            }
                        }

                        // Keep Data List Start
                        composable(
                            route = BottomNavigation.List.route
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("ProjectList")
                            }
                        }
                        // Keep Data List End
                        //setting Screen Start
                        composable(
                            route = BottomNavigation.Settings.route
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Settings")
                            }
                        }
                        //setting Screen End
                    }
                }
            }
        }
    }
}
fun pxToDp(px: Float): Float {
    val metrics = Resources.getSystem().displayMetrics
    return px / metrics.density
}
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    EPDCTheme {
//        Greeting("Android")
//    }
//}