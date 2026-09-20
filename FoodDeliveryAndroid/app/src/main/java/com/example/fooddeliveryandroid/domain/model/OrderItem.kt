package com.example.fooddeliveryandroid.domain.model

import java.math.BigDecimal

data class OrderItem (
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val productName: String,
    val priceAtPurchase: BigDecimal,
    val productImageUrl: String?
)