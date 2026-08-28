package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import com.example.fooddeliveryandroid.data.local.room.relation.ProductWithCategory
import com.example.fooddeliveryandroid.data.remote.dto.ProductCartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import com.example.fooddeliveryandroid.data.remote.dto.ShortProductResponse
import com.example.fooddeliveryandroid.domain.model.Product
import com.example.fooddeliveryandroid.presentation.navigation.Screen

object ProductMapper {
    fun responseToModel(productResponse: ProductResponse): Product{
        return Product(
            id = productResponse.id,
            name = productResponse.name,
            description = productResponse.description,
            imageUrl = productResponse.imageUrl,
            price = productResponse.price,
            weight = productResponse.weight,
            calories = productResponse.calories,
            category = CategoryMapper.responseToModel(productResponse.category),
            available = productResponse.available
        )
    }

    fun responseToEntity(productResponse: ShortProductResponse): ProductEntity {
        return ProductEntity(
            id = productResponse.id,
            name = productResponse.name,
            imageUrl = productResponse.imageUrl,
            price = productResponse.price,
            weight = productResponse.weight,
            categoryId = productResponse.category.id,
            available = productResponse.available
        )
    }

    fun productWithCategoryToProduct(productWithCategory: ProductWithCategory): Product {
        return Product (
            id = productWithCategory.product.id,
            name = productWithCategory.product.name,
            imageUrl = productWithCategory.product.imageUrl,
            price = productWithCategory.product.price,
            weight = productWithCategory.product.weight,
            category = CategoryMapper.entityToModel(productWithCategory.category),
            available = productWithCategory.product.available
        )
    }

    fun responseToModel(productResponse: ShortProductResponse): Product {
        return Product(
            id = productResponse.id,
            name = productResponse.name,
            imageUrl = productResponse.imageUrl,
            price = productResponse.price,
            category = CategoryMapper.responseToModel(productResponse.category),
            available = productResponse.available
        )
    }
//
//    fun withProductToProduct(productEntity: ProductEntity): Product {
//        return Product (
//                id = productEntity.id,
//                name = productEntity.name,
//                imageUrl = productEntity.imageUrl,
//                price = productEntity.price,
//                weight = productEntity.weight,
//                category = productEntity.,
//                available = productWithCategory.product.available
//
//        )
//    }

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