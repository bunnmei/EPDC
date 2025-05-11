package space.webkombinat.epdc.Model.DB.Project

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import space.webkombinat.epdc.Model.DB.Rect.RectDao
import space.webkombinat.epdc.Model.DB.Text.TextDao

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val textDao: TextDao,
    private val rectDao: RectDao,
) {
    suspend fun insertProject(project: ProjectEntity): Long {
        return projectDao.create(project)
    }

    suspend fun updateProject(project: ProjectEntity) {
        projectDao.update(project)
    }

    suspend fun deleteProject(project: ProjectEntity) {
        projectDao.delete(project)
        textDao.deleteByProjectId(project.id)
        rectDao.deleteByProjectId(project.id)
    }

    fun getAll(): Flow<List<ProjectEntity>> {
        return projectDao.getAll()
    }
}