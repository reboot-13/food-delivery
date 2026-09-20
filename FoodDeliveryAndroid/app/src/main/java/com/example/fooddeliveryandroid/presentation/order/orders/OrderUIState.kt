package com.example.fooddeliveryandroid.presentation.order.orders

import com.example.fooddeliveryandroid.domain.model.Order

sealed class OrderUIState {
    data class Success(val orders: List<Order>): OrderUIState()

    data object Loading: OrderUIState()

    data class Error(val message: String): OrderUIState()
}