package com.example.fooddeliveryandroid.presentation.cart

import com.example.fooddeliveryandroid.domain.model.CartItem

sealed class CartUIState {

    data class Success(val cartItems: List<CartItem>): CartUIState()

    data class Error(val message: String): CartUIState()

    data object Loading: CartUIState()

    data object Unauthorized: CartUIState()
}