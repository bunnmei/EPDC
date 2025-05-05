package space.webkombinat.epdc.Model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Label

sealed class BottomNavigation(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {

    object Canvas: BottomNavigation(route = "canvas", icon = Icons.Default.Palette, label = "CANVAS")
    object List: BottomNavigation(route = "projects", icon = Icons.Default.List, label = "LIST")
    object Settings: BottomNavigation(route = "setting", icon = Icons.Default.Settings, label = "SETTING")
}

