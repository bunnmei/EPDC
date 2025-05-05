package space.webkombinat.epdc.VIew.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import space.webkombinat.epdc.Model.Controller.ACTION_USB_PERMISSION

@Composable
fun SystemBroadcastReceiver(
    onSystemEvent: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    DisposableEffect(context) {
        val intentFilter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
//            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(ACTION_USB_PERMISSION)
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}
