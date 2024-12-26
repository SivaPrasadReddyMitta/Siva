package uk.ac.tees.mad.univid.Utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.AppNavigationComponent
import java.util.Locale

val yellowColor = Color(0xFFFDD563)
val whiteColor = Color(0xFFFAF7CC)
val darkPurpleColor = Color(0xFF54309E)
val lightPurpleColor = Color(0xFFCDC1FF)
val lightPurpleColorA = Color(0xFF585A89)

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

fun getAddressFromLocation(context: Context, location: Location, onChange: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
    if (addresses != null) {
        Log.d("Address", addresses.toString())
        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            Log.d("Address", address.toString())
            // Use address.getAddressLine(0) to get the full address or other methods as needed
            val addressString = address.getAddressLine(0)
            // Update your TextField or state with the address
            Log.d("Address", addressString.toString())
            onChange(addressString)
        }
    }
}