package space.webkombinat.epdc.Model.DB.Text

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface TextDao {
    @Insert
    suspend fun create(text: TextEntity): Long

    @Update
    suspend fun update(text: TextEntity)

    @Delete
    suspend fun delete(text: TextEntity)
}