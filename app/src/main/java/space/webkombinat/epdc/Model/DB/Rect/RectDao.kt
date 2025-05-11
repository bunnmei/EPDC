package space.webkombinat.epdc.Model.DB.Rect

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import space.webkombinat.epdc.Model.DB.Text.TextEntity

@Dao
interface RectDao {
    @Insert
    suspend fun create(rect: RectEntity): Long

    @Update
    suspend fun update(text: RectEntity)

    @Delete
    suspend fun delete(text: RectEntity)

    @Query("DELETE FROM rect WHERE project_id_rect = :projectId")
    suspend fun deleteByProjectId(projectId: Long)
}