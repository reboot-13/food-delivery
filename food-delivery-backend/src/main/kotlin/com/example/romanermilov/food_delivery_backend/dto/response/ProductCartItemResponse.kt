package com.example.romanermilov.food_delivery_backend.dto.response

import java.math.BigDecimal

data class ProductCartItemResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val available: Boolean
)
