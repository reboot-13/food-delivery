package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CartItemRepository: JpaRepository<CartItemEntity, Long> {

    fun findByUserId(userId: Long): List<CartItemEntity>

    fun findByProductIdAndUserId(productId: Long, userId: Long): Optional<CartItemEntity>

    fun deleteByUserIdAndProductId(userId: Long, productId: Long)
}