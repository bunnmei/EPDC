package space.webkombinat.epdc.Model.DB.Text

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import space.webkombinat.epdc.Model.CanvasObjects.TextDate
import space.webkombinat.epdc.Model.Constants

@Entity(tableName = "text")
class TextEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "project_id_text")
    val projectId: Long,
    var text: String,
    var x: Int,
    var y: Int,
    var fontSize: Int,
    var fontWeight: Int,
    var fontFamily: String,
    var colorMode: Int
) {
    fun toTextDate(num: Long): TextDate =
        TextDate(
            id = num,
            text = text,
            x = x,
            y = y,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            colorMode = Constants.intToColorMode(colorMode)
        )
}