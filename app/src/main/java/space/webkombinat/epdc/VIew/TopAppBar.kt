package space.webkombinat.epdc.VIew

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.OperateType

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    click: () -> Unit,
    click_s: (Color) -> Unit,
    vm: CanvasVM
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.End
    ) {
            Row(
                modifier = modifier.weight(1f).height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (OperateType.Text == vm.operate_data_type.value && vm.text_items.isNotEmpty()) {
                    val obj = vm.text_items[vm.operate_data_id.value-1]
                    Row (
                        modifier = modifier.fillMaxSize().clickable{
                            click_s(obj.color)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${obj.text} - ${obj.id}",
                        )
                    }

                } else if (OperateType.Rect == vm.operate_data_type.value && vm.rect_items.isNotEmpty()) {
                    val obj = vm.rect_items[vm.operate_data_id.value-1]
                    Row (
                        modifier = modifier.fillMaxSize().clickable{
                            click_s(obj.color)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Box(
                            modifier = modifier.height(16.dp)
                                .width(16.dp)
                                .background(obj.color)
                        )
                        Spacer(modifier = modifier.width(8.dp))
                        Text("Rect - ${obj.id}")
                    }

                }
            }

        Box(
            modifier = modifier
                .width(60.dp)
                .height(60.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable{
                    click()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "object list Open"
            )
        }

    }
}