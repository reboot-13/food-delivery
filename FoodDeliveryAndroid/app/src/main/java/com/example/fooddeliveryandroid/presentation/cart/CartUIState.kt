package com.example.fooddeliveryandroid.presentation.cart

import com.example.fooddeliveryandroid.domain.model.CartItem

sealed class CartUIState {

    data class Success(val data: List<CartItem>): CartUIState()

    data class Error(val message: String): CartUIState()

    data object Loading: CartUIState()
}