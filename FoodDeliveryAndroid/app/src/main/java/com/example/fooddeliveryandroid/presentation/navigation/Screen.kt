package com.example.fooddeliveryandroid.presentation.navigation

sealed class Screen (route: String){

    object Catalog : Screen("catalog")

    object Profile : Screen("profile")

    object Cart : Screen("cart")

    object Product : Screen("product/{productId}")

    object Order : Screen("order/{orderId}")

    object Orders : Screen("orders")
}