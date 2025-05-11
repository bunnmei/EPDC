package space.webkombinat.epdc.Model.DB.Rect

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import space.webkombinat.epdc.Model.CanvasObjects.RectData
import space.webkombinat.epdc.Model.Constants

@Entity(tableName = "rect")
data class RectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "project_id_rect")
    val projectId: Long,
    var x: Int,
    var y: Int,
    var size_w: Int,
    var size_h: Int,
    var degree: Int,
    var colorMode: Int
) {
    fun toRectDate(num: Long): RectData = RectData(
        id = num,
        x = x,
        y = y,
        size_w = size_w,
        size_h = size_h,
        degree = degree,
        colorMode = Constants.intToColorMode(colorMode)
    )

}