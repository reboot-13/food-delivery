package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<CategoryEntity, Long>