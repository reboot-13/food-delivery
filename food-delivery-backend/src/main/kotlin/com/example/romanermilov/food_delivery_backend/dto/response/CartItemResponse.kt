package com.example.romanermilov.food_delivery_backend.dto.response

data class CartItemResponse (
    val product: ProductCartItemResponse,
    val quantity: Short,
)