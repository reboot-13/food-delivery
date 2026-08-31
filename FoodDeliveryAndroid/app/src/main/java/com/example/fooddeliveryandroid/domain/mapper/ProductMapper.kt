package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import com.example.fooddeliveryandroid.data.local.room.relation.ProductWithCategory
import com.example.fooddeliveryandroid.data.remote.dto.ProductCartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import com.example.fooddeliveryandroid.domain.model.Product

object ProductMapper {
    fun responseToEntity(productResponse: ProductResponse): ProductEntity {
        return ProductEntity(
            id = productResponse.id,
            name = productResponse.name,
            imageUrl = productResponse.imageUrl,
            price = productResponse.price,
            weight = productResponse.weight,
            categoryId = productResponse.category.id,
            available = productResponse.available,
            description = productResponse.description,
            calories = productResponse.calories
        )
    }

    fun productWithCategoryToProduct(productWithCategory: ProductWithCategory): Product {
        return Product (
            id = productWithCategory.product.id,
            name = productWithCategory.product.name,
            imageUrl = productWithCategory.product.imageUrl,
            description = productWithCategory.product.description,
            calories = productWithCategory.product.calories,
            price = productWithCategory.product.price,
            weight = productWithCategory.product.weight,
            category = CategoryMapper.entityToModel(productWithCategory.category),
            available = productWithCategory.product.available
        )
    }


    fun responseToModel(productResponse: ProductCartItemResponse): Product {
        return Product(
            id = productResponse.id,
            name = productResponse.name,
            imageUrl = productResponse.imageUrl,
            price = productResponse.price,
            available = productResponse.available
        )
    }
}