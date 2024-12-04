package uk.ac.tees.mad.univid.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.Utils.ShakeDetector
import uk.ac.tees.mad.univid.Utils.navigateWithBackStack

@Composable
fun HomeScreen(navController: NavController, vm: MainViewModel) {
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
        Column(modifier = Modifier.padding(it)) {
            Text(text = "HomeScreen")
        }
    }
}