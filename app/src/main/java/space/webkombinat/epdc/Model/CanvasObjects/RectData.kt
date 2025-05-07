package space.webkombinat.epdc.Model.CanvasObjects

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.android.parcel.Parcelize
import space.webkombinat.epdc.ViewModel.ColorMode

@Parcelize
data class RectData(
    val id: Int,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var color: Int,
    var colorMode: ColorMode
) : Parcelable {
    fun getColor(): Color = when(color) {
        0 -> Color.Black
        1 -> Color.Red
        else -> {
            Color.Black
        }
    }

}