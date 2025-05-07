package space.webkombinat.epdc.VIew

import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.CanvasVM

@Composable
fun FontDropDownMenu(
    modifier: Modifier = Modifier,
    vm: CanvasVM
) {
    var expanded by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    val uiState by vm.uiState.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(66.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(uiState.textItems[uiState.operateIndex-1].fontFamily)
        IconButton(onClick = {
//            vm.fontListGet(ctx = ctx)
            println("font size ${vm.fontFolderReader.font_list.size}")
            expanded = !expanded
        }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            vm.fontFolderReader.font_list.forEach { fontName ->
                DropdownMenuItem(
                    text = { Text(fontName) },
                    onClick = {
                        vm.changeFont(ctx = ctx, fontName = fontName)
                    }
                )
            }
        }
    }
}

