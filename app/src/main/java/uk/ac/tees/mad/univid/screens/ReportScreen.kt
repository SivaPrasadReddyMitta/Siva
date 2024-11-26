package uk.ac.tees.mad.univid.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.Utils.darkPurpleColor
import uk.ac.tees.mad.univid.ui.theme.poppins
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(navController: NavController, vm : MainViewModel) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var location by remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val scope = rememberCoroutineScope()
    val file = context.createImageFile()
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
        capturedImageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it)
        {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }
        else
        {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

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


    Scaffold(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.navigationBars),
        bottomBar = {
            BottomNavBar(selectedItem = BottomNavItems.ReportScreen, navController = navController)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(20.dp))
            if (capturedImageUri.path?.isNotEmpty() == true)
            {
                Image(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp, 8.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = null
                )
            }
            else
            {
                Image(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp, 8.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null
                )
            }
            Button(onClick = { val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED)
                {
                    cameraLauncher.launch(uri)
                }
                else
                {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }, colors = ButtonDefaults.buttonColors(darkPurpleColor)) {
                Text(text = "Capture Image")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column {
                Text(text = "TITLE", fontFamily = poppins, fontWeight = FontWeight.Bold)
                TextField(value = title, onValueChange = { title = it },singleLine = true,modifier = Modifier.width(280.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ))
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column {
                Text(text = "DESCRIPTION", fontFamily = poppins, fontWeight = FontWeight.Bold)
                TextField(value = description, onValueChange = { description = it },modifier = Modifier.width(280.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ))
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column {
                Text(text = "LOCATION", fontFamily = poppins, fontWeight = FontWeight.Bold)
                TextField(value = location, onValueChange = { location = it },singleLine = true, modifier = Modifier.width(280.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ))
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(darkPurpleColor)) {
                Text(text = "Submit")
            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
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