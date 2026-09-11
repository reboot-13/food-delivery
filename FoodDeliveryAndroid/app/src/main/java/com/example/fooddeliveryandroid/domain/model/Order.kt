package com.example.fooddeliveryandroid.domain.model

import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    val id: Long,
    val user: User,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val totalPrice: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
