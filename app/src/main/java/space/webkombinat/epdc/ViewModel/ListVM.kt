package space.webkombinat.epdc.ViewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.webkombinat.epdc.Model.DB.Project.ProjectRepository
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    val projectRepo: ProjectRepository,
): ViewModel() {
    val projectList = projectRepo.getAll()
}