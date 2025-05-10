package space.webkombinat.epdc.Model.DB.Project

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun insertProject(project: ProjectEntity): Long {
        projectDao.create(project)
        return project.id
    }

    fun getAll(): Flow<List<ProjectEntity>> {
        return projectDao.getAll()
    }
}