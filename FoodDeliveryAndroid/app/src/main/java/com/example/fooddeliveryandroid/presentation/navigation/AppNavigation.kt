package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryandroid.presentation.splash.SplashScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable (route = Screen.MainScreen.route){
            MainScaffold(modifier)
        }
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onGoToMainScaffold = {
                    rootNavController.navigate(Screen.MainScreen.route) {
                        popUpTo(route = Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}