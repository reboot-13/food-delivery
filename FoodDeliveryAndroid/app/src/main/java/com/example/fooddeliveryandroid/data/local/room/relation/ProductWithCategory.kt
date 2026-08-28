package com.example.fooddeliveryandroid.data.local.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fooddeliveryandroid.data.local.entity.CategoryEntity
import com.example.fooddeliveryandroid.data.local.entity.ProductEntity

data class ProductWithCategory(
    @Embedded
    val product: ProductEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id")
    val category: CategoryEntity
)