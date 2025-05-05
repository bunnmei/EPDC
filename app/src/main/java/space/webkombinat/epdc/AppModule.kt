package space.webkombinat.epdc

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import space.webkombinat.epdc.Model.Controller.CanvasManager
import space.webkombinat.epdc.Model.Controller.UsbController

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
    fun provideUsbController(): UsbController {
        return UsbController()
    }
}