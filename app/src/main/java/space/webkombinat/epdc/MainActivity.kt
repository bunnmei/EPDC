package space.webkombinat.epdc

import android.graphics.Rect
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
import jakarta.inject.Inject
import space.webkombinat.epdc.Model.BottomNavigation
import space.webkombinat.epdc.Model.Controller.ACTION_USB_PERMISSION
import space.webkombinat.epdc.Model.Controller.UsbController
import space.webkombinat.epdc.VIew.BottomFloatingButton
import space.webkombinat.epdc.VIew.BottomNavigationBar
import space.webkombinat.epdc.VIew.CanvasDraw.CanvasPreview
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_rect
import space.webkombinat.epdc.VIew.CanvasDraw.Canvas_text
import space.webkombinat.epdc.VIew.CanvasTab
import space.webkombinat.epdc.VIew.EDPCanvas
import space.webkombinat.epdc.VIew.Screen.ListScreen
import space.webkombinat.epdc.VIew.Screen.SettingsScreen
import space.webkombinat.epdc.VIew.OperateBottomSheet
import space.webkombinat.epdc.VIew.OperateButtons
import space.webkombinat.epdc.VIew.Receiver.SystemBroadcastReceiver
import space.webkombinat.epdc.VIew.RectDataEditor
import space.webkombinat.epdc.VIew.Screen.CanvasScreen
import space.webkombinat.epdc.VIew.SideObjectList
import space.webkombinat.epdc.VIew.TextDataEditor
import space.webkombinat.epdc.VIew.TopAppBar
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.ColorMode
import space.webkombinat.epdc.ViewModel.ListVM
import space.webkombinat.epdc.ViewModel.OperateType
import space.webkombinat.epdc.ViewModel.SettingsVM
import space.webkombinat.epdc.ui.theme.EPDCTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var usb: UsbController
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EPDCTheme {
                val ctx = LocalContext.current
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                var selectedItem = rememberSaveable { mutableStateOf(1) }
//                var prevbitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController,
                            selectedItem = selectedItem
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
                ) {
                    innerPadding ->

                    SystemBroadcastReceiver { intent ->
                        when (intent?.action) {
                            ACTION_USB_PERMISSION -> {
                                val manager = ctx.getSystemService(USB_SERVICE) as? UsbManager
                                if (manager!!.deviceList.isNotEmpty()) {
                                    val device = manager.deviceList?.values?.first()
                                    val hasPermission = manager.hasPermission(device)
                                    if (hasPermission) {
                                        usb.setup()
                                    }
                                }
                            }
                            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                                println("接続された")
                            }
                            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                                println("切断された")
                            }
                        }

                    }
                    NavHost(
                        navController = navController,
//                        startDestination = BottomNavigation.Canvas.route,
                        startDestination = BottomNavigation.List.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(
                            route = BottomNavigation.Canvas.route,
                        ){ backStackEntry ->
                            val canvasVM = hiltViewModel<CanvasVM>()
                            CanvasScreen(vm = canvasVM)
                        }

                        composable(
                            route = BottomNavigation.List.route
                        ) {
                            val listVM = hiltViewModel<ListVM>()
                            ListScreen(
                                vm = listVM,
                                navController = navController
                            ) {
                                selectedItem.value = 0
                            }
                        }

                        composable(
                            route = BottomNavigation.Settings.route
                        ) {
                            val settingsVM = hiltViewModel<SettingsVM>()
                            SettingsScreen(vm = settingsVM)
                        }

                    }
                }
            }
        }
    }

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