package com.example.romanermilov.food_delivery_backend.mapper
import com.example.romanermilov.food_delivery_backend.dto.response.ProductResponse
import com.example.romanermilov.food_delivery_backend.entity.ProductEntity

object ProductMapper {
    fun toResponse(product: ProductEntity): ProductResponse {
        return ProductResponse(
            id = product.id!!,
            name = product.name,
            description = product.description,
            price = product.price,
            imageUrl = product.imageUrl,
            weight = product.weight,
            calories = product.calories,
            available = product.available,
            category = CategoryMapper.toShortResponse(product.category))
    }
}