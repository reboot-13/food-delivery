package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.response.CategoryResponse
import com.example.romanermilov.food_delivery_backend.mapper.CategoryMapper
import com.example.romanermilov.food_delivery_backend.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService (private val categoryRepository: CategoryRepository) {
    fun getAllCategories(): List<CategoryResponse>{
        return categoryRepository.findAll()
            .map ( CategoryMapper::toResponse )
    }
}