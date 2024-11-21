package uk.ac.tees.mad.univid.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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