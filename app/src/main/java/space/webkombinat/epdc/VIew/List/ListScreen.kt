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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.Model.DB.Project.ProjectEntity
import space.webkombinat.epdc.ViewModel.ListVM


@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    vm: ListVM
) {

    val projectLists by vm.projectList.collectAsState(initial = emptyList())
    val openEditDialog = remember { mutableStateOf(false) }
    val editItem = remember { mutableStateOf<ProjectEntity?>(null) }
    Box(
        modifier = modifier.fillMaxSize(),

    ) {
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(projectLists.size) {
                ListItem(
                    text ="${projectLists[it].projectName ?: "NoTitle"} - ${projectLists[it].id}",
                    delete = { vm.deleteProject(project = projectLists[it]) },
                    rename = {
                        editItem.value = projectLists[it]
                        openEditDialog.value = true
                    }
                )
            }
        }

        if (openEditDialog.value) {
            Box( // Mask
                modifier = modifier.fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f))
                    .clickable {openEditDialog.value = false},
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(0.8f)
//                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (editItem.value != null) {
                        OutlinedTextField(
                            value =
                                if (editItem.value!!.projectName == null || editItem.value!!.projectName == "") {""}
                                else {editItem.value!!.projectName.toString()},
                            onValueChange = { newText ->
                                editItem.value = editItem.value?.copy(projectName = newText)
                            }
                        )
                        Spacer(modifier = modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = {
                                openEditDialog.value = false
                                editItem.value = null
                            }) {
                                Text("キャンセル")
                            }
                            Button(onClick = {
                                vm.updateProject(editItem.value!!)
                                openEditDialog.value = false
                                editItem.value = null
                            }) {
                                Text("保存")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    text: String,
    delete: () -> Unit,
    rename: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
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
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clickable {
                    expanded = !expanded
                }
        ){
            Icon(
                contentDescription = "edit",
                imageVector = Icons.Default.MoreVert,
            )
            DropdownMenu(
                offset = DpOffset(x = 0.dp, y = 8.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        rename()
                    },
                    text = { Text("名前を変更") },
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        delete()
                    },
                    text = { Text("削除") },
                )
            }
        }


    }
}

