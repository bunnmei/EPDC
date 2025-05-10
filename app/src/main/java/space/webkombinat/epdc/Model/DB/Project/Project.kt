package space.webkombinat.epdc.Model.DB.Project

import java.util.Date

data class Project(
    val id: Long,
    var projectName: String?,
    val createdAt: Date,
)
