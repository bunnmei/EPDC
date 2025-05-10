package space.webkombinat.epdc.Model.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import space.webkombinat.epdc.Model.DB.Project.ProjectDao
import space.webkombinat.epdc.Model.DB.Project.ProjectEntity
import space.webkombinat.epdc.Model.DB.Rect.RectDao
import space.webkombinat.epdc.Model.DB.Rect.RectEntity
import space.webkombinat.epdc.Model.DB.Text.TextDao
import space.webkombinat.epdc.Model.DB.Text.TextEntity

@Database(
    entities = [ProjectEntity::class, TextEntity::class, RectEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ProjectDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun textDao(): TextDao
    abstract fun rectDao(): RectDao
}