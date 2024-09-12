package com.example.geminitest1

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.geminitest1.ui.screens.AddBikeLayout
import com.example.geminitest1.ui.screens.DashBoardLayout
import com.example.geminitest1.ui.screens.OnBoardingScreenLayout
import com.example.geminitest1.ui.screens.SplashScreen


@Composable
fun AppNavHost(navHostController: NavHostController){


    NavHost(navController = navHostController,startDestination = AppConstants.Screens.SPLASH ){
        composable(route = AppConstants.Screens.SPLASH){
            SplashScreen(navController = navHostController)
        }
        composable(route = AppConstants.Screens.ONBOARDINGSCREEN){
            OnBoardingScreenLayout(navController = navHostController)
        }
        composable(route=AppConstants.Screens.ADDBIKESCREEN){
            AddBikeLayout(navController = navHostController)
        }
/*        composable(route=AppConstants.Screens.ECRScreen){
            ECRScreenLayout(navController=navHostController, innerPadding = PaddingValues())
        }*/
        composable(route=AppConstants.Screens.DASHBOARDSCREEN){
            DashBoardLayout(navController=navHostController)
        }
    }
}