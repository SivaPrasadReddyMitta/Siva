package uk.ac.tees.mad.univid.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.OvershootInterpolator
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.AppNavigationComponent
import uk.ac.tees.mad.univid.MainViewModel
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.Utils.navigateWithoutBackStack

@Composable
fun SplashScreen(navController: NavController, vm : MainViewModel) {

    val isSignedIn = vm.isSignedIn
    val visible = remember { androidx.compose.runtime.mutableStateOf(true) }
    val scale = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(2f).getInterpolation(it) }
            )
        )
        delay(3000) // Splash screen delay
        visible.value = false
        if (isSignedIn.value) {
            navigateWithoutBackStack(navController, AppNavigationComponent.Home)
        } else {
            navigateWithoutBackStack(navController, AppNavigationComponent.Login)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible.value) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .scale(scale.value)
            )
        }
    }
}