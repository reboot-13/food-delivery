package com.example.fooddeliveryandroid.presentation.navigation

sealed class Screen (val route: String){

    data object Catalog : Screen("catalog")

    data object Profile : Screen("profile")

    data object Cart : Screen("cart")

    data object Product : Screen("product/{productId}")

    data object Order : Screen("order/{orderId}")

    data object Orders : Screen("orders")

    data object Splash : Screen("splash")

    data object MainScreen: Screen(route = "mainScreen")
}