package space.webkombinat.epdc.Model.CanvasObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import space.webkombinat.epdc.Model.DB.Text.TextEntity
import space.webkombinat.epdc.ViewModel.ColorMode
import space.webkombinat.epdc.Model.Constants

@Parcelize
data class TextDate(
    val id: Long,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var fontFamily: String,
    var colorMode: ColorMode
) : Parcelable {

    fun toTextEntity(itemId : Long = 0, projectId: Long): TextEntity =
        TextEntity(
            id = itemId,
            projectId = projectId,
            text = text,
            x = x,
            y = y,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            colorMode = Constants.colorModeToInt(colorMode),
        )


//    fun colorModeToInt(colorMode: ColorMode): Int {
//        return when (colorMode) {
//            ColorMode.Black -> 1
//            ColorMode.Red -> 2
//        }
//    }
}