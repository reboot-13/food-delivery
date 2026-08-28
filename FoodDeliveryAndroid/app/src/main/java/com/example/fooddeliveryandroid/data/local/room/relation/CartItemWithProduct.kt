package com.example.fooddeliveryandroid.data.local.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import java.math.BigDecimal

data class CartItemWithProduct(
    val productId: Long,
    val quantity: Int,

    val productName: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val weight: Short?,
    val available: Boolean,

    val categoryId: Long,
    val categoryName: String
)