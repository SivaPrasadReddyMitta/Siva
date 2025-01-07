package uk.ac.tees.mad.univid.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.Utils.darkPurpleColor
import uk.ac.tees.mad.univid.Utils.navigateWithBackStack

enum class BottomNavItems(
    val destination : AppNavigationComponent,
    val icon : ImageVector
){
    HomeScreen(AppNavigationComponent.Home, Icons.Filled.Home),
    ReportScreen(AppNavigationComponent.Report, Icons.Filled.Create),
    ProfileScreen(AppNavigationComponent.Profile, Icons.Filled.Person)
}

@Composable
fun BottomNavBar(selectedItem : BottomNavItems, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkPurpleColor), verticalAlignment = Alignment.Bottom
    ) {


        for (items in BottomNavItems.entries) {
            Image(
                imageVector = items.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable { navigateWithBackStack(navController = navController,items.destination) },
                colorFilter = if (selectedItem == items){
                    ColorFilter.tint(color = Color.White)
                }
                else{
                    ColorFilter.tint(Color.Black)
                })
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    // Get the current backstack entry to find the current route
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val navItems = listOf(
        NavigateItems(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            destination = AppNavigationComponent.Home
        ),
        NavigateItems(
            title = "Report",
            selectedIcon = Icons.Filled.Create,
            unSelectedIcon = Icons.Outlined.Create,
            destination = AppNavigationComponent.Report
        ),
        NavigateItems(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unSelectedIcon = Icons.Outlined.Person,
            destination = AppNavigationComponent.Profile
        )
    )

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            // Check if the current destination matches the item's destination
            val isSelected = currentDestination?.route == item.destination.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.destination.route) {
                            // Handle the back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
data class NavigateItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val destination: AppNavigationComponent
)
