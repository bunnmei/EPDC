package space.webkombinat.epdc.VIew

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.OperateType

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ObjectAccordion(modifier: Modifier = Modifier) {

    var openList by remember { mutableStateOf(OperateType.Text) }

    BoxWithConstraints {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val targetWidthPx = maxWidthPx - with(LocalDensity.current) { 50.dp.toPx() }
        val targetWidthDp = with(LocalDensity.current) { targetWidthPx.toDp() }
    }

}