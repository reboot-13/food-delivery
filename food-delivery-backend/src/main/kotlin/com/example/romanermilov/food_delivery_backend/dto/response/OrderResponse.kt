package com.example.romanermilov.food_delivery_backend.dto.response
import com.example.romanermilov.food_delivery_backend.entity.enum.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse (
    val id: Long,
    val userId: Long,
    val items: List<OrderItemResponse>,
    val status: OrderStatus,
    val totalPrice: BigDecimal,
    val createdAt: LocalDateTime,
    )