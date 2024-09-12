package com.example.geminitest1.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.geminitest1.AppConstants
import com.example.geminitest1.R
import com.example.geminitest1.viewmodel.BikeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController,
                 bikeViewModel: BikeViewModel = hiltViewModel()){
    val coroutineScope = rememberCoroutineScope()
    val hasBikes by bikeViewModel.hasBikes().collectAsState()

    val animationDuration = 2000L

    // Animation states
    val angle = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            // Start animations
            launch {
                angle.animateTo (
                    targetValue = 360f,
                    animationSpec = tween(
                        durationMillis = animationDuration.toInt(),
                        easing = LinearEasing
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = animationDuration.toInt(),
                        easing = LinearEasing
                    )
                )
            }
            delay(animationDuration)
            if (hasBikes && bikeViewModel.bikes.value.isNotEmpty()){
                navController.navigate(AppConstants.Screens.DASHBOARDSCREEN){
                    popUpTo(AppConstants.Screens.SPLASH){
                        inclusive=true
                    }
                }
            }
            else{
                navController.navigate(AppConstants.Screens.ONBOARDINGSCREEN){
                        popUpTo(AppConstants.Screens.SPLASH){
                            inclusive=true
                        }
                }
            }
        }
    }
    //Ui Code here --
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.error_bike_app_icon),
                contentDescription = stringResource(id = R.string.app_logo),
                modifier = Modifier
                    .size(250.dp)
                    .rotate(angle.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BIKE ERROR RESOLVER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.alpha(alpha.value)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(navController = rememberNavController())
}