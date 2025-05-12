package space.webkombinat.epdc

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import androidx.room.Room
import space.webkombinat.epdc.Model.Controller.CanvasManager
import space.webkombinat.epdc.Model.Controller.FontFolderReader
import space.webkombinat.epdc.Model.Controller.UsbController
import space.webkombinat.epdc.Model.CurrentEditData
import space.webkombinat.epdc.Model.DB.Project.ProjectDao
import space.webkombinat.epdc.Model.DB.ProjectDatabase
import space.webkombinat.epdc.Model.DB.Rect.RectDao
import space.webkombinat.epdc.Model.DB.Text.TextDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    fun provideCanvasManager(@ApplicationContext context: Context): CanvasManager {
        val displayMetrics = context.resources.displayMetrics
        return CanvasManager(
            screenWidth = displayMetrics.widthPixels,
            screenHeight = displayMetrics.heightPixels,
            screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt(),
            screenHeightDp = (displayMetrics.heightPixels / displayMetrics.density).toInt(),
        )
    }

    @Provides
    @Singleton
    fun provideUsbController(): UsbController {
        return UsbController()
    }

    @Provides
    @Singleton
    fun provideFontFolderReader(@ApplicationContext context: Context): FontFolderReader {
        val fontFolderReader: FontFolderReader = FontFolderReader()
        fontFolderReader.createFontFolder(ctx = context)
        return fontFolderReader
    }

    @Provides
    @Singleton
    fun provideCurrentEditData(): CurrentEditData {
        return CurrentEditData()
    }

    //db
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProjectDatabase {
        return Room.databaseBuilder(
            context,
            ProjectDatabase::class.java,
            "project_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: ProjectDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideTextDao(database: ProjectDatabase): TextDao {
        return database.textDao()
    }

    @Provides
    @Singleton
    fun provideRectDao(database: ProjectDatabase): RectDao {
        return database.rectDao()
    }

}