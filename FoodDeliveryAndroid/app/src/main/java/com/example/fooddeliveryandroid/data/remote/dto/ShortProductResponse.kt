package com.example.fooddeliveryandroid.data.remote.dto

import java.math.BigDecimal

data class ShortProductResponse (
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val category: CategoryShortResponse,
    val price: BigDecimal,
    val weight: Short?,
    val available: Boolean
)