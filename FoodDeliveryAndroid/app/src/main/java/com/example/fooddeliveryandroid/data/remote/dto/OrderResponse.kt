package com.example.fooddeliveryandroid.data.remote.dto

import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
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