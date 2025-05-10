package space.webkombinat.epdc.VIew.List

import android.widget.ScrollView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.ListVM


@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    vm: ListVM
) {

    val vm = vm.projectList.collectAsState(initial = emptyList())
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(vm.value.size) {
            ListItem(text = vm.value[it].projectName ?: "NoTitle")
        }
//        items(100) {
////            Text(text = "Item $it")
//            ListItem(text = "Item $it")
//        }
//        item {
//            Spacer(modifier = Modifier.height(8.dp))
//        }
    }

}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
        Spacer(modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(50.dp)
                .height(50.dp)
                .clickable {

                }
        ){
            Icon(
                contentDescription = "edit",
                imageVector = Icons.Default.MoreVert,
            )
        }
    }
}

