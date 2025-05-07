package space.webkombinat.epdc.VIew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import space.webkombinat.epdc.ViewModel.CanvasVM

@Composable
fun OperateButtons(
    modifier: Modifier = Modifier,
    vm: CanvasVM,
    click: () -> Unit
) {
    val ctx = LocalContext.current
    Row(
        modifier = Modifier.height(66.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if(vm.usbController.connected.value == false) {
            BasicButton(
                icon = Pair(Icons.Filled.UsbOff, "usb setup"),
                click = {vm.usbConnectionSetup(ctx = ctx)}
            )
        } else {
            BasicButton(
                icon = Pair(Icons.Filled.Usb, "usb data transfer"),
                click = {vm.usbDataTransfer()},
                enabled = !vm.usbController.transferState.value
            )
        }

        BasicButton(
            icon = Pair(Icons.Default.Download, "save edited data"),
            click = {}
        )

        BasicButton(
            icon = Pair(Icons.Default.DocumentScanner, "convert to 296*128"),
            click = {click()}
        )
    }
}

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    icon: Pair<ImageVector, String>,
    click: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            enabled = enabled,
            onClick = {click()},
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        ) {
            Icon(
                imageVector = icon.first,
                contentDescription = icon.second
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }

}