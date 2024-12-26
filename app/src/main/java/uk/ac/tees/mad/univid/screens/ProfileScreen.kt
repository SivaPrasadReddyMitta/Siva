package uk.ac.tees.mad.univid.screens

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.Utils.getAddressFromLocation
import uk.ac.tees.mad.univid.Utils.lightPurpleColor
import uk.ac.tees.mad.univid.Utils.navigateWithoutBackStack
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(navController: NavController, vm: MainViewModel) {
    val isLoading = vm.isLoading
    val user = vm.userData
    var name by remember {
        mutableStateOf(user.value?.name?:"")
    }
    var email by remember {
        mutableStateOf(user.value?.email?:"")
    }
    var phonenumber by remember {
        mutableStateOf(user.value?.phonenumber?:"")
    }
    var location by remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    fun getCurrentLocation() {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGpsEnabled || isNetworkEnabled) {
            Log.d("GPS", "GPS ENABLED")
            if (locationPermissionState.status == PermissionStatus.Granted) {
                Log.d("GPS", "GPS ENABLED2")
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    Log.d("Location", "Location Found")
                    Log.d("Location", "${loc?.latitude}, ${loc?.longitude}")
                    loc?.let {
                        val locString = "Lat: ${it.latitude}, Lon: ${it.longitude}"
                        getAddressFromLocation(context = context, location = loc, onChange = {
                            location = it
                        })
                        Log.d("Location", locString)
                    }
                }
            } else {
                Log.d("GPS", "GPS NOT ENABLED 1")

                locationPermissionState.launchPermissionRequest()
            }
        } else {
            Log.d("GPS", "GPS NOT ENABLED")

            Toast.makeText(context, "Please turn on GPS", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = true) {
        getCurrentLocation()
    }

    Scaffold (modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.navigationBars),
        bottomBar = {
            BottomNavBar(selectedItem = BottomNavItems.ProfileScreen, navController = navController)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Row(Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .clickable {
                            navigateWithoutBackStack(
                                navController = navController,
                                AppNavigationComponent.Home
                            )
                        })
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Image(painter = painterResource(id = R.drawable.user), contentDescription = null, modifier = Modifier.size(110.dp))
                Spacer(modifier = Modifier.height(55.dp))
                Row {
                    Icon(imageVector = Icons.Default.Person,tint = lightPurpleColor, contentDescription = null, modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically))
                    TextField(value = name, onValueChange = {name = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Icon(imageVector = Icons.Default.Email, tint = lightPurpleColor, contentDescription = null, modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically))
                    TextField(value = email, onValueChange = {email = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Icon(imageVector = Icons.Default.Phone, tint = lightPurpleColor, contentDescription = null, modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically))
                    TextField(value = phonenumber, onValueChange = {phonenumber = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.width(310.dp)) {
                    Icon(imageVector = Icons.Default.LocationOn, tint = lightPurpleColor, contentDescription = null, modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically))
                    TextField(value = location, onValueChange = {location = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { vm.updateUserData(name, email, phonenumber) },modifier = Modifier.width(310.dp),
                    colors = ButtonDefaults.buttonColors(lightPurpleColor), shape = RoundedCornerShape(4.dp) ) {
                    if (isLoading.value) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "SAVE")
                    }
                }
            }
        }
    }
}
