package com.example.fooddeliveryandroid.presentation.navigation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fooddeliveryandroid.R
import com.example.fooddeliveryandroid.presentation.navigation.Screen

sealed class BottomDestination (
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Cart : BottomDestination (
        route = Screen.Cart.route,
        icon = Icons.Default.ShoppingCart,
        title = "Корзина"
    )

    data object Catalog : BottomDestination (
        route = Screen.Catalog.route,
        icon = Icons.Default.Home,
        title = "Заказ"
    )

    data object Profile: BottomDestination (
        route = Screen.Profile.route,
        icon = Icons.Default.AccountCircle,
        title = "Профиль"
    )
}