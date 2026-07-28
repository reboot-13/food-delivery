package com.example.fooddeliveryandroid.data.remote.dto

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