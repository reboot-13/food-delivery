package com.example.fooddeliveryandroid.presentation.navigation

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.example.fooddeliveryandroid.presentation.address.AddressesScreen
import com.example.fooddeliveryandroid.presentation.address.LocalAddAddressNavigation
import com.example.fooddeliveryandroid.presentation.address.addAddress.AddAddressScreen
import com.example.fooddeliveryandroid.presentation.address.addressForm.AddressFormScreen
import com.example.fooddeliveryandroid.presentation.address.map.AddressMapPicker
import com.example.fooddeliveryandroid.presentation.cart.CartScreen
import com.example.fooddeliveryandroid.presentation.catalog.CatalogScreen
import com.example.fooddeliveryandroid.presentation.navigation.bottomBar.BottomBar
import com.example.fooddeliveryandroid.presentation.order.orderDetails.OrderDetailsScreen
import com.example.fooddeliveryandroid.presentation.order.orders.OrderScreen
import com.example.fooddeliveryandroid.presentation.profile.ProfileScreen


private const val ADDRESS_MAP_SELECTION_KEY = "address_map_selection"
@Composable
fun MainScaffold(
    modifier: Modifier
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val exceptBottomBarScreenRoutes = listOf("order/", "addAddress")

    val showBottomBar = exceptBottomBarScreenRoutes.none { destination ->
        currentRoute?.startsWith(destination) == true
    }



    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    currentRoute = currentRoute,
                    onRouteChange = { route ->
                        mainNavController.navigate(route)
                    }
                )
            }
        }
    ) { innerPadding ->

//        Нужно для навигации на экран без кучи колбэков при нажатии на кнопку добавление адреса
        CompositionLocalProvider(
            LocalAddAddressNavigation provides {
                mainNavController.navigate(Screen.AddAddress.route)
            }
        ) {
            NavHost(
                navController = mainNavController,
                startDestination = Screen.Catalog.route,
                modifier = modifier.padding(
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

                composable( route = Screen.AddAddress.route ) { navBackStackEntry ->
                    AddAddressScreen(
                        onEnterManually = {
                            navBackStackEntry.savedStateHandle
                                .remove<AddressMapSelection>(ADDRESS_MAP_SELECTION_KEY)

                            mainNavController.navigate(Screen.AddressForm.route)
                        },
                        onContinueToForm = { selection ->
                            navBackStackEntry.savedStateHandle[
                                ADDRESS_MAP_SELECTION_KEY
                            ] = selection

                            mainNavController.navigate(Screen.AddressForm.route)
                        }
                    )
                }

                composable ( route = Screen.AddressForm.route) { navBackStackEntry ->
                    val mapSelection = remember(navBackStackEntry) {
                        mainNavController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<AddressMapSelection>(ADDRESS_MAP_SELECTION_KEY)

                    }
                    AddressFormScreen(mapSelection)
                }
            }
        }
    }
}