package space.webkombinat.epdc.VIew

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import space.webkombinat.epdc.Model.BottomNavigation


@Composable
fun BottomNavigationBar(
    navController: NavController,
    selectedItem: MutableState<Int>,
    items: List<BottomNavigation> = listOf(
        BottomNavigation.Canvas,
        BottomNavigation.List,
        BottomNavigation.Settings
    ),
    onItemClick: (BottomNavigation) -> Unit
) {

    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = Modifier.height(60.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem.value == index,
                onClick = {
                    selectedItem.value = index
                    onItemClick(item)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor= MaterialTheme.colorScheme.primary,
                    indicatorColor= MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor= MaterialTheme.colorScheme.outline,
                    unselectedTextColor= MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}