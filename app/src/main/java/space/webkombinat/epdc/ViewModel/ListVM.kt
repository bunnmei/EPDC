package space.webkombinat.epdc.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import space.webkombinat.epdc.Model.CurrentEditData
import space.webkombinat.epdc.Model.DB.Project.ProjectEntity
import space.webkombinat.epdc.Model.DB.Project.ProjectRepository
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    val projectRepo: ProjectRepository,
    val currentEditData: CurrentEditData,
): ViewModel() {
    val projectList = projectRepo.getAll()

    fun saveProjectId(id: Long) {
        currentEditData.selectProject(id)
    }
    fun updateProject(project: ProjectEntity) {
        viewModelScope.launch {
            projectRepo.updateProject(project)
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            projectRepo.deleteProject(project)
        }
    }
}