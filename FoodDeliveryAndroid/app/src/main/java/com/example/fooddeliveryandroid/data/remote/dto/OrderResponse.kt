package com.example.fooddeliveryandroid.data.remote.dto

import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import java.math.BigDecimal

data class OrderResponse (
    val id: Long,
    val userId: Long,
    val items: List<OrderItemResponse>,
    val status: OrderStatus,
    val totalPrice: BigDecimal,
    val createdAt: String,
    )