package uk.ac.tees.mad.univid.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.Utils.navigateWithoutBackStack
import uk.ac.tees.mad.univid.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    vm: MainViewModel,
    itemId: String?,
    itemTitle: String?,
    itemDescription: String?,
    itemImage: String?,
    itemLocation: String?,
    phoneNumber: String?,
    name: String?
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Row(modifier = Modifier.padding(start = 1.dp)) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null, modifier = Modifier
                    .size(34.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        navigateWithoutBackStack(navController, AppNavigationComponent.Home)
                    }
                )
                Text(text = "Lost item")
            }})
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scroll)) {
            Column(Modifier.padding(start = 45.dp, end = 45.dp, top = 25.dp)) {
                AsyncImage(model = itemImage ?: "", contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.height(480.dp))
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = itemTitle ?: "", fontFamily = poppins, fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp)
                Row {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
                    Text(text = itemLocation ?: "", fontFamily = poppins)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = itemDescription ?: "", fontFamily = poppins)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "User Informations", fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Text(text = ("Name: " + name), fontFamily = poppins)
                Row {
                    Button(onClick = { 
                        val callUri = Uri.parse("tel:$phoneNumber")
                        val intent = Intent(Intent.ACTION_DIAL, callUri)
                        context.startActivity(intent)
                    }) {
                        Text(text = "Call Now")
                    }
                    Button(onClick = { 
                        val smsUri = Uri.parse("sms:$phoneNumber")
                        val intent = Intent(Intent.ACTION_SENDTO, smsUri)
                        intent.putExtra("sms_body", "Hello, I am interested in your item")
                        context.startActivity(intent)}) {
                        Text(text = "Send Message")
                    }
                }
            }
        }
    }
}