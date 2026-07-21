package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.CategoryResponse
import com.example.romanermilov.food_delivery_backend.dto.response.CategoryShortResponse
import com.example.romanermilov.food_delivery_backend.entity.CategoryEntity

object CategoryMapper {
    fun toResponse(category: CategoryEntity): CategoryResponse {
        return CategoryResponse(category.id!!, category.name)
    }

    fun toShortResponse(category: CategoryEntity): CategoryShortResponse {
        return CategoryShortResponse(category.id!!, category.name)
    }
}