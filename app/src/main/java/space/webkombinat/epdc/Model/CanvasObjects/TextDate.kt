package space.webkombinat.epdc.Model.CanvasObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import space.webkombinat.epdc.ViewModel.ColorMode

@Parcelize
data class TextDate(
    val id: Int,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var fontFamily: String,
    var colorMode: ColorMode
) : Parcelable