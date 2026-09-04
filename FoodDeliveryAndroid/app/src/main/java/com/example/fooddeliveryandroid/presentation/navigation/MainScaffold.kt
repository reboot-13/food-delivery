package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fooddeliveryandroid.presentation.cart.CartScreen
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.navigation.bottomBar.BottomBar
import com.example.fooddeliveryandroid.presentation.product.ProductScreen
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
            startDestination = Screen.Catalog.route,
            modifier = modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
            )
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
            composable ( Screen.Cart.route ) {
                CartScreen(
                    onGoToAuthScreen = {
                        mainNavController.navigate(Screen.Profile.route)
                    }
                )
            }
        }
    }
}