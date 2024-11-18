package uk.ac.tees.mad.univid

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.univid.screens.DetailsScreen
import uk.ac.tees.mad.univid.screens.HomeScreen
import uk.ac.tees.mad.univid.screens.LoginScreen
import uk.ac.tees.mad.univid.screens.ProfileScreen
import uk.ac.tees.mad.univid.screens.RegisterScreen
import uk.ac.tees.mad.univid.screens.ReportScreen
import uk.ac.tees.mad.univid.screens.SplashScreen

sealed class AppNavigationComponent(val route: String){
    object Splash: AppNavigationComponent("splash")
    object Login: AppNavigationComponent("login")
    object Register: AppNavigationComponent("register")
    object Home: AppNavigationComponent("home")
    object Details: AppNavigationComponent("details")
    object Profile: AppNavigationComponent("profile")
    object Report : AppNavigationComponent("report")
}


@Composable
fun AppNavigation() {
    val viewModel : MainViewModel = viewModel()
    val navController = rememberNavController()
    Surface {
    NavHost(navController = navController, startDestination = AppNavigationComponent.Splash.route ) {
        composable(route = AppNavigationComponent.Splash.route) {
            SplashScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Login.route) {
            LoginScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Register.route) {
            RegisterScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Home.route) {
            HomeScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Details.route) {
            DetailsScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Profile.route) {
            ProfileScreen(navController = navController, vm = viewModel)
        }
        composable(route = AppNavigationComponent.Report.route) {
            ReportScreen(navController = navController, vm = viewModel)
        }
    }
    }
}