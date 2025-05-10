package space.webkombinat.epdc.Model.DB.Project

import androidx.room.Embedded
import androidx.room.Relation
import space.webkombinat.epdc.Model.DB.Rect.RectEntity
import space.webkombinat.epdc.Model.DB.Text.TextEntity

data class ProjectRelation(
    @Embedded
    val project: ProjectEntity,

    @Relation(parentColumn = "id", entityColumn = "project_id_text")
    val textItems: List<TextEntity> = emptyList(),

    @Relation(parentColumn = "id", entityColumn = "project_id_rect")
    val rectItems: List<RectEntity> = emptyList()
)
