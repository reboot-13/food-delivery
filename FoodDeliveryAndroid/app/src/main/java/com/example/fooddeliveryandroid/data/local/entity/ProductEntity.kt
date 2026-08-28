package com.example.fooddeliveryandroid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "products",
)
data class ProductEntity (
    @PrimaryKey
    val id: Long,

    val name: String,

    val price: BigDecimal,

    val imageUrl: String?,

    val weight: Short?,

    val available: Boolean,

    val categoryId: Long
)
