package space.webkombinat.epdc.Model.CanvasObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import space.webkombinat.epdc.ViewModel.ColorMode

@Parcelize
data class RectData(
    val id: Int,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var colorMode: ColorMode
) : Parcelable