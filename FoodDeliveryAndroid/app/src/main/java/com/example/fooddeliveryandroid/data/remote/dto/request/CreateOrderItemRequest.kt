package com.example.fooddeliveryandroid.data.remote.dto.request

data class CreateOrderItemRequest (
    val productId: Long,
    val quantity: Int
)