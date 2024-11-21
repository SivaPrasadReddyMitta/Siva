package uk.ac.tees.mad.univid.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.Utils.darkPurpleColor
import uk.ac.tees.mad.univid.Utils.navigateWithBackStack
import uk.ac.tees.mad.univid.Utils.navigateWithoutBackStack
import uk.ac.tees.mad.univid.Utils.yellowColor
import uk.ac.tees.mad.univid.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, vm: MainViewModel) {
    val isSignedIn = vm.isSignedIn
    val isLoading = vm.isLoading
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    if (isSignedIn.value){
        navigateWithoutBackStack(navController,AppNavigationComponent.Home)
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Sign Up.", fontFamily = poppins, fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp, modifier = Modifier.padding(top = 80.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Text(text = "NAME", fontFamily = poppins, fontWeight = FontWeight.Bold)
            TextField(value = name, onValueChange = { name = it }, singleLine = true, colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ))
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Text(text = "EMAIL", fontFamily = poppins, fontWeight = FontWeight.Bold)
            TextField(value = email, onValueChange = { email = it },singleLine = true, colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ))
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Text(text = "PASSWORD", fontFamily = poppins, fontWeight = FontWeight.Bold)
            TextField(value = password, onValueChange = { password = it },singleLine = true,
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
            Text(text = "PHONE", fontFamily = poppins, fontWeight = FontWeight.Bold)
            TextField(value = phone, onValueChange = { phone = it },singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ))
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.length < 10 ) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else {
                vm.signUp(
                    context = context,
                    name = name,
                    email = email,
                    password = password,
                    phone = phone
                )
            }        }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(
            yellowColor
        ) , modifier = Modifier
            .height(50.dp)
            .width(280.dp)) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Text(text = "Sign Up")
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Row {
            Text(text = "Already have an account?", fontFamily = poppins)
            Text(text = " Sign In", fontFamily = poppins, fontWeight = FontWeight.SemiBold, color = darkPurpleColor,
                modifier = Modifier.clickable {
                    navigateWithoutBackStack(navController = navController, route = AppNavigationComponent.Login)
                })
        }
    }
}