package com.example.fooddeliveryandroid.presentation.cart

sealed class CartEvent {
    data class OrderCreated(val orderId: Long): CartEvent()
}