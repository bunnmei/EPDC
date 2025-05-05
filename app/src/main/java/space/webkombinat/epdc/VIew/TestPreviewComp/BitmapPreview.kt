package space.webkombinat.epdc.VIew.TestPreviewComp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
fun BitmapPreview(
    modifier: Modifier = Modifier,
    bitmap :ImageBitmap?
) {
    if(bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = "Bitmap Image"
        )
    }
}