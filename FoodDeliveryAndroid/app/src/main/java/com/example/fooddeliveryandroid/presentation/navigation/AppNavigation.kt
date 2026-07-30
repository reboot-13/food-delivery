package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.profile.ProfileScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "profile",
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
    }
}