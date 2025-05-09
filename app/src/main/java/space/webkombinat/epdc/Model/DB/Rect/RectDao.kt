package space.webkombinat.epdc.Model.DB.Rect

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface RectDao {
    @Insert
    suspend fun create(rect: RectEntity): Long
}