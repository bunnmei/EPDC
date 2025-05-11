package space.webkombinat.epdc.Model

import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrentEditData @Inject constructor() {
    private val _selectedProjectId = MutableStateFlow<Long?>(null)
    val selectedProjectId: StateFlow<Long?> = _selectedProjectId

    fun selectProject(id: Long?) {
        _selectedProjectId.value = id
    }
}