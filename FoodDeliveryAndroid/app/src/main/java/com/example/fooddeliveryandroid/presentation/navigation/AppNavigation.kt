package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.profile.ProfileScreen
import com.example.fooddeliveryandroid.presentation.splash.SplashScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("catalog") {
            CatalogScreen()
        }
        composable ("profile") {
            ProfileScreen(
                onShowOrders = {
                    navController.navigate("orders")
                }
            )
        }
        composable ( "splash" ) {
            SplashScreen(
                onGoToCatalog = {
                    navController.navigate("profile") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}