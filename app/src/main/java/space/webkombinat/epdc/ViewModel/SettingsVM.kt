package space.webkombinat.epdc.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.webkombinat.epdc.Model.Controller.FontFolderReader
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    val fontFolderReader: FontFolderReader,
): ViewModel() {
    fun getFontPath(ctx: Context): String {
        return fontFolderReader.createFontFolder(ctx = ctx).absolutePath
    }
}