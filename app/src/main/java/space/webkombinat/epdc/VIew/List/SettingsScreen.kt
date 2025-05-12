package space.webkombinat.epdc.VIew.List

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.webkombinat.epdc.ViewModel.SettingsVM
import java.io.File
import kotlin.contracts.contract

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    vm: SettingsVM
) {
    val ctx  = LocalContext.current
    var filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {  uri: Uri? ->
            if (uri != null) {
                val fileName = getFileNameFromUri(ctx, uri)
                println("filename: $fileName")
                val inputStream = ctx.contentResolver.openInputStream(uri)
                val targetFile = File(vm.getFontPath(ctx), fileName)
                println("filepaht: ${targetFile.absolutePath}")
                inputStream?.use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Font",
            fontSize = 12.sp,
//            modifier = modifier
//                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                   )
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
    //                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    //                    addCategory(Intent.CATEGORY_OPENABLE)
    //                    type = "*/*"
    //                }
                    filePickerLauncher.launch(arrayOf("*/ttf"))
                }
            ) {
                Text("フォントインポート .ttf")
            }
        }
        Text(
            text = "FontFilePath",
            fontSize = 12.sp,
//            modifier = modifier
//                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        Text(
            text = "FontPath /storage/emulated/0/Android/data/space.webkombinat.epdc/files/fonts"
        )
    }
//    Column {
//        Button(
//            onClick = {
////                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
////                    addCategory(Intent.CATEGORY_OPENABLE)
////                    type = "*/*"
////                }
//                filePickerLauncher.launch(arrayOf("*/*"))
//            }
//        ) {
//            Text("Fontをインポート")
//        }
//    }

}
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var result = "imported_file"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    result = it.getString(nameIndex)
                }
            }
        }
        return result
    }
