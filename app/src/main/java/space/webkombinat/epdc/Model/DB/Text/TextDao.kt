package space.webkombinat.epdc.Model.DB.Text

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TextDao {
    @Insert
    suspend fun create(text: TextEntity): Long
}