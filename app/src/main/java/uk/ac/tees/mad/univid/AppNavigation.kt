package uk.ac.tees.mad.univid

import android.net.Uri
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    object Details : AppNavigationComponent("details/{itemId}/{itemTitle}/{itemDescription}/{itemImage}/{itemLocation}/{phoneNumber}/{name}") {
        fun createRoute(
            itemId: String,
            itemTitle: String,
            itemDescription: String,
            itemImage: String,
            itemLocation: String,
            phoneNumber: String,
            name: String
        ): String {
            return "details/${Uri.encode(itemId)}/${Uri.encode(itemTitle)}/${Uri.encode(itemDescription)}/${Uri.encode(itemImage)}/${Uri.encode(itemLocation)}/${Uri.encode(phoneNumber)}/${Uri.encode(name)}"
        }
    }
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
        composable(
            route = AppNavigationComponent.Details.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType },
                navArgument("itemTitle") { type = NavType.StringType },
                navArgument("itemDescription") { type = NavType.StringType },
                navArgument("itemImage") { type = NavType.StringType },
                navArgument("itemLocation") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            val itemTitle = backStackEntry.arguments?.getString("itemTitle")
            val itemDescription = backStackEntry.arguments?.getString("itemDescription")
            val itemImage = backStackEntry.arguments?.getString("itemImage")
            val itemLocation = backStackEntry.arguments?.getString("itemLocation")
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")
            val name = backStackEntry.arguments?.getString("name")
            DetailsScreen(
                navController = navController,
                vm = viewModel,
                itemId = itemId,
                itemTitle = itemTitle,
                itemDescription = itemDescription,
                itemImage = itemImage,
                itemLocation = itemLocation,
                phoneNumber = phoneNumber,
                name = name
            )
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