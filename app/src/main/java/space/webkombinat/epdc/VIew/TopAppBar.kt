package space.webkombinat.epdc.VIew

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    click: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.End
    ) {
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