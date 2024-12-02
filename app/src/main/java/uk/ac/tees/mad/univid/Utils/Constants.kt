package uk.ac.tees.mad.univid.Utils

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.AppNavigationComponent

val yellowColor = Color(0xFFFDD563)
val whiteColor = Color(0xFFFAF7CC)
val darkPurpleColor = Color(0xFF54309E)
val lightPurpleColor = Color(0xFFCDC1FF)

const val USERS = "users"
const val ITEMS = "items"


fun navigateWithoutBackStack(navController: NavController, route: AppNavigationComponent){
    navController.navigate(route.route){
        popUpTo(0)
    }
}

fun navigateWithBackStack(navController: NavController, route: AppNavigationComponent){
    navController.navigate(route.route)
}
