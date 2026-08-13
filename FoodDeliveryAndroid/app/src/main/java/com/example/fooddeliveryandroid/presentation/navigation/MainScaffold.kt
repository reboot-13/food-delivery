package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.navigation.bottomBar.BottomBar
import com.example.fooddeliveryandroid.presentation.profile.ProfileScreen

@Composable
fun MainScaffold(
    modifier: Modifier
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            BottomBar(
                currentRoute = currentRoute,
                onRouteChange = { route ->
                    mainNavController.navigate(route)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Screen.Profile.route, //change to Catalog later
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Catalog.route) {
                CatalogScreen()
            }
            composable (Screen.Profile.route) {
                ProfileScreen(
                    onShowOrders = {
                        mainNavController.navigate(Screen.Orders.route)
                    }
                )
            }
        }
    }
}