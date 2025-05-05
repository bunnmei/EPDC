package space.webkombinat.epdc.VIew

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import space.webkombinat.epdc.Model.ColorSet
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.ColorMode

@Composable
fun CanvasTab(
    modifier: Modifier = Modifier,
    tabList: List<ColorMode>,
    vm: CanvasVM
) {
    val uiState by vm.uiState.collectAsState()
    TabRow(
        selectedTabIndex = tabList.indexOfFirst { it == uiState.selectTab},
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        tabList.forEachIndexed { index, item ->
            Tab(
                selected = uiState.selectTab == item,
                onClick = {
                    vm.setTabMode(item)
                },
                text = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.selectTab == item)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
            )
        }

    }
}