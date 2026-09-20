package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddeliveryandroid.presentation.cart.CartScreen
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.navigation.bottomBar.BottomBar
import com.example.fooddeliveryandroid.presentation.orders.OrderDetailsScreen
import com.example.fooddeliveryandroid.presentation.orders.OrderScreen
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
                CatalogScreen(
                    onNavigateToAuthScreen = {
                        mainNavController.navigate(Screen.Profile.route)
                    }
                )
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
                    },
                    onGoToCatalog = {
                        mainNavController.navigate(Screen.Catalog.route)
                    },
                    onShowOrder = { orderId ->
                        mainNavController.navigate(Screen.Order.createRoute(orderId))
                    }
                )
            }
            composable ( Screen.Orders.route ) {
                OrderScreen()
            }

            composable(
                route = Screen.Order.route,
                arguments = listOf(
                    navArgument("orderId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val orderId =
                    backStackEntry.arguments?.getLong("orderId")
                        ?: return@composable

                OrderDetailsScreen(
                    orderId = orderId,
                    onGoBack = {
                        mainNavController.popBackStack()
                    }
                )
            }
        }
    }
}