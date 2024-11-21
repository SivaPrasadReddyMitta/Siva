package uk.ac.tees.mad.univid.screens


import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.R
import java.io.File
import java.util.Locale



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReportScreen(navController: NavController, vm : MainViewModel) {
    val image = remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    Scaffold {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            image.let {
                AsyncImage(model =image, contentDescription =null)
            }.run { 
                Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = null)
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Capture Image")
            }
        }
    }
}

private fun createImageUri(context: Context): Uri? {
    val image = File(context.filesDir, "camera_photo.png")
    return FileProvider.getUriForFile(context, "uk.ac.tees.mad.univid.provider", image)
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