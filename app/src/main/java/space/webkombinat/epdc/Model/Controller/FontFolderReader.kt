package space.webkombinat.epdc.Model.Controller

import android.R
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import java.io.File
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty

class FontFolderReader() {
    val folderName = "FontFolder"
    var defaultFontList = listOf<String>(
        FontFamily.Default.toString(),
        FontFamily.Monospace.toString(),
        FontFamily.Serif.toString(),
        FontFamily.SansSerif.toString(),
        FontFamily.Cursive.toString()
    )
    var font_list = mutableStateListOf<String>()

    fun createFontFolder(ctx: Context) {
        val appSpecificExternalDir = File(ctx.getExternalFilesDir(null), folderName)
        if (!appSpecificExternalDir.exists()) {
            if (appSpecificExternalDir.mkdirs()) {
                Log.d("MyApp", "フォルダを作成しました: ${appSpecificExternalDir.absolutePath} (Compose)")
            } else {
                Log.e("MyApp", "フォルダの作成に失敗しました。 (Compose)")
            }
        } else {
            Log.d("MyApp", "フォルダは既に存在します: ${appSpecificExternalDir.absolutePath} (Compose)")
            createFontFileList(file = appSpecificExternalDir)
        }
    }

    fun FontFolderReload(ctx: Context) {
        println("FontFolderReloadが呼び出されたよ")
        createFontFolder(ctx = ctx)
    }

    private fun createFontFileList(file: File){
        val contents = file.listFiles()
        font_list.clear()
        println("createFontFileListが呼び出されたよ")
        if (contents != null) {
            println("contents はNullでない")
            if (contents.isNotEmpty()) {

                val new_font_list = mutableListOf<String>()
                contents.forEach { file ->
                    // へんなフォルダ名でないか、チェックする必要がある
                    if (file.exists() && file.canRead()) {
                        new_font_list.add("${file.name}")
                    }
                }
                val defFontAndFileFont = defaultFontList + new_font_list
                println("defFontAndFileFont = ${defFontAndFileFont.size}")
                font_list.addAll(defFontAndFileFont)
            } else {
                font_list.addAll(defaultFontList)
            }
        } else {
            font_list.addAll(defaultFontList)
            println("contentsがnullだよ ${font_list.size}")
        }
    }

    fun getFontFamily(ctx: Context, fileName: String): FontFamily {
        val file = File(ctx.getExternalFilesDir(null), "$folderName/$fileName")
        if (file.exists() && file.canRead()) {
            return FontFamily(Font(file))
        }

        return FontFamily.Default
    }
}