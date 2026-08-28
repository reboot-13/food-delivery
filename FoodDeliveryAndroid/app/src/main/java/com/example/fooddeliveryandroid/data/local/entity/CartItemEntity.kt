package com.example.fooddeliveryandroid.data.local.entity

import androidx.room.Entity
import com.example.fooddeliveryandroid.domain.model.Product

@Entity (
    tableName = "cart_items",
    primaryKeys = ["productId"]
)
data class CartItemEntity (
    val productId: Long,
    val quantity: Int
)