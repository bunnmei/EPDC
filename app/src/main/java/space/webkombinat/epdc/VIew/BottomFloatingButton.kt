package space.webkombinat.epdc.VIew

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomFloatingButton(
    modifier: Modifier = Modifier,
    click: () -> Unit
) {
    Row {
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = click
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
