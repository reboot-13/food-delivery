package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.OrderEntity
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<OrderEntity, Long> {
    fun findOrdersByUser(user: UserEntity): List<OrderEntity>
}