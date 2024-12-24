package uk.ac.tees.mad.univid.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, vm: MainViewModel) {
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
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(70.dp))
                Image(painter = painterResource(id = R.drawable.user), contentDescription = null, modifier = Modifier.size(150.dp))
                Spacer(modifier = Modifier.height(55.dp))
                Row {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    TextField(value = name, onValueChange = {name = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    TextField(value = email, onValueChange = {email = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                    TextField(value = phonenumber, onValueChange = {phonenumber = it},
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor= Color.Transparent,
                            errorCursorColor = Color.Transparent))
                }
            }
        }
    }
}