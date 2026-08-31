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

    val description: String?,

    val price: BigDecimal,

    val imageUrl: String?,

    val weight: Short?,

    val calories: Short? ,

    val available: Boolean,

    val categoryId: Long
)
