package uk.ac.tees.mad.univid.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.widget.GridLayout.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.Utils.ShakeDetector
import uk.ac.tees.mad.univid.Utils.darkPurpleColor
import uk.ac.tees.mad.univid.Utils.lightPurpleColor
import uk.ac.tees.mad.univid.Utils.lightPurpleColorA
import uk.ac.tees.mad.univid.Utils.navigateWithBackStack
import uk.ac.tees.mad.univid.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
                 TopAppBar(title = { Text(text = "Items", fontFamily = poppins, fontWeight = FontWeight.Bold) })
        },
        bottomBar = {
            BottomNavBar(selectedItem = BottomNavItems.HomeScreen, navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(items.value) { item ->
                    ItemView(image = item.image, title = item.title, location = item.location){
                        navController.navigate(AppNavigationComponent.Details.createRoute(
                            itemId = item.id,
                            itemTitle = item.title,
                            itemDescription = item.description,
                            itemImage = item.image,
                            itemLocation = item.location,
                            phoneNumber = item.number,
                            name = item.name
                        ))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(image: String, title: String, location: String, onItemClick: () -> Unit) {
    Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { onItemClick() }) {
        Column(modifier = Modifier.height(370.dp)) {
            AsyncImage(
                model = image, contentDescription = null,
                contentScale = ContentScale.FillWidth, modifier = Modifier.height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontFamily = poppins, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text(text = location, fontFamily = poppins, fontWeight = FontWeight.Light, fontSize = 10.sp)
            }
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(lightPurpleColor)
        ) {
            Text(text = "Share")
        }
    }
}