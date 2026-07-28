package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import com.example.fooddeliveryandroid.data.remote.dto.ShortProductResponse
import com.example.fooddeliveryandroid.domain.model.Product

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
}