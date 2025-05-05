package space.webkombinat.epdc.VIew

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import space.webkombinat.epdc.Model.ColorSet

@Composable
fun CanvasTab(
    modifier: Modifier = Modifier,
    tabList: List<ColorSet>,
    selectedTabIndex: MutableState<Int>,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex.value-1,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        tabList.forEachIndexed { index, item ->

            Tab(
                selected = selectedTabIndex.value == item.id,
                onClick = {
                    selectedTabIndex.value = item.id
                    println("item.id ${item.id}")
                },
                text = {
                    Text(
                        text = item.colorName,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTabIndex.value == item.id)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
            )
        }

    }
}