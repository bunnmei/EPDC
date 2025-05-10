package space.webkombinat.epdc.Model.DB.Project

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun create(project: ProjectEntity): Long

    @Update
    suspend fun update(profile: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT * FROM project order by createdAt desc")
    fun getAll(): Flow<List<ProjectEntity>>

    @Transaction
    @Query("SELECT * FROM project WHERE id = :id")
    suspend fun profileAndTextAndRect(id: Long): ProjectRelation?
}