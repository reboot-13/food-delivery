package com.example.romanermilov.food_delivery_backend.repository
import com.example.romanermilov.food_delivery_backend.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ProductRepository: JpaRepository<ProductEntity, Long> {
    fun findByAvailableTrue(): List<ProductEntity>
    fun findByCategoryIdAndAvailableTrue(categoryId: Long): List<ProductEntity>
    fun findByIdAndAvailableTrue(productId: Long): Optional<ProductEntity>
}