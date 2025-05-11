package space.webkombinat.epdc.Model.CanvasObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import space.webkombinat.epdc.Model.Constants
import space.webkombinat.epdc.Model.DB.Rect.RectEntity
import space.webkombinat.epdc.ViewModel.ColorMode


@Parcelize
data class RectData(
    val id: Long,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var colorMode: ColorMode
) : Parcelable {
    fun toRectEntity(itemId : Long = 0, projectId: Long): RectEntity = RectEntity(
        id = itemId,
        projectId = projectId,
        x = x,
        y = y,
        size_w = size_w,
        size_h = size_h,
        degree = degree,
        colorMode = Constants.colorModeToInt(colorMode),
    )
}