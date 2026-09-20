package com.example.fooddeliveryandroid.presentation.orders

import com.example.fooddeliveryandroid.domain.model.Order

sealed class OrderDetailsUIState {
    data object Loading: OrderDetailsUIState()

    data class Error(val message: String): OrderDetailsUIState()

    data class Success(val order: Order) : OrderDetailsUIState()
}