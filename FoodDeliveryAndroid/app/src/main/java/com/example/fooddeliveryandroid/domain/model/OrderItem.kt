package com.example.fooddeliveryandroid.domain.model

import java.math.BigDecimal

data class OrderItem (
    val id: Long,
    val order: Order,
    val product: Product,
    val quantity: Int,
    val productName: String,
    val priceAtPurchase: BigDecimal,
    val productImageUrl: String
)