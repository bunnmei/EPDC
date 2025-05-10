package space.webkombinat.epdc.Model.DB.Project

import jakarta.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun insertProject(project: ProjectEntity): Long {
        projectDao.create(project)
        return project.id
    }
}