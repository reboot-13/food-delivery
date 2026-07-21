package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.response.CategoryResponse
import com.example.romanermilov.food_delivery_backend.service.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): List<CategoryResponse>{
        return categoryService.getAllCategories()
    }

}