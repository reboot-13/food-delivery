package com.example.romanermilov.food_delivery_backend.dto.response

data class CreateOrderItemRequest (
    val productId: Long,
    val quantity: Int
)