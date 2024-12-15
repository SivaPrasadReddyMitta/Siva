package uk.ac.tees.mad.univid.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.Utils.ShakeDetector
import uk.ac.tees.mad.univid.Utils.navigateWithBackStack

@Composable
fun HomeScreen(navController: NavController, vm: MainViewModel) {
    val items = vm.item
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val shakeDetector = ShakeDetector(
        onShake = {
            navigateWithBackStack(navController, AppNavigationComponent.Report)
        },
        context = context
    )

    DisposableEffect(Unit) {
        sensorManager.registerListener(
            shakeDetector,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(shakeDetector)
        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.navigationBars),
        bottomBar = {
            BottomNavBar(selectedItem = BottomNavItems.HomeScreen, navController = navController)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items.value) { item->
                    ItemView(image = item.image, title = item.title, location = item.location)
                }
            }
        }
    }
}

@Composable
fun ItemView(image: String, title: String,location: String){
    Column(modifier = Modifier.height(400.dp)) {
        AsyncImage(model = image, contentDescription = null,
            contentScale = ContentScale.FillWidth, modifier = Modifier.height(200.dp))
        Text(text = title)
        Text(text = location)
    }
}

@Preview(showBackground = true)
@Composable
fun showData(){
    ItemView(image = "https://firebasestorage.googleapis.com/v0/b/lost2found-13849.appspot.com/o/items%2FJPEG_2024_08_02_12%3A11%3A31_2245757208242690281.jpg?alt=media&token=3d19af26-5f4c-4095-ac03-58919aed8145",
        title = "Airpods Pro",
        location = "University of York")
}