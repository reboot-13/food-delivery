package com.example.romanermilov.food_delivery_backend.dto.response

import java.math.BigDecimal

data class OrderItemResponse (
    val id: Long,
    val productId: Long,
    val productName: String,
    val productImageUrl: String?,
    val priceAtPurchase: BigDecimal,
    val quantity: Int
)