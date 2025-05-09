package space.webkombinat.epdc.Model.DB.Project

import androidx.room.Embedded
import androidx.room.Relation
import space.webkombinat.epdc.Model.DB.Rect.RectEntity
import space.webkombinat.epdc.Model.DB.Text.TextEntity

data class ProjectRelation(
    @Embedded val profile: ProjectEntity,
    val project: ProjectEntity,

    @Relation(parentColumn = "id", entityColumn = "projectId")
    val textItmes: List<TextEntity> = emptyList(),

    @Relation(parentColumn = "id", entityColumn = "projectId")
    val rectItems: List<RectEntity> = emptyList()
)
