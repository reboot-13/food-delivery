package com.example.romanermilov.food_delivery_backend.dto.response

import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val imageUrl: String?,
    val weight: Short?,
    val calories: Short?,
    val available: Boolean,
    val category: CategoryShortResponse
)
