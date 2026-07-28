package com.example.fooddeliveryandroid.domain.model

import com.example.fooddeliveryandroid.data.remote.dto.CategoryShortResponse
import java.math.BigDecimal

data class Product (
    val id: Long,
    val name: String,
    val description: String? = null,
    val price: BigDecimal,
    val imageUrl: String? = null,
    val weight: Short? = null,
    val calories: Short? = null,
    val available: Boolean,
    val category: Category
)