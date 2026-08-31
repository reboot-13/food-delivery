package com.example.fooddeliveryandroid.data.local.room.relation

import java.math.BigDecimal

data class CartItemWithProduct(
    val productId: Long,
    val quantity: Int,

    val productName: String,
    val description: String?,
    val calories: Short?,
    val price: BigDecimal,
    val imageUrl: String?,
    val weight: Short?,
    val available: Boolean,


    val categoryId: Long,
    val categoryName: String
)