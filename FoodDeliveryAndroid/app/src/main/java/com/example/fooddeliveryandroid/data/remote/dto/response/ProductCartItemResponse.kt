package com.example.fooddeliveryandroid.data.remote.dto.response

import java.math.BigDecimal

data class ProductCartItemResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val available: Boolean
)
