package com.example.romanermilov.food_delivery_backend.dto.request

data class CreateOrderItemRequest (
    val productId: Long,
    val quantity: Int
)