package com.example.fooddeliveryandroid.domain.model

import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    val id: Long,
    val userId: Long,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val orderType: OrderType,
    val totalPrice: BigDecimal,

    val deliveryAddress: String?,
    val deliveryLatitude: BigDecimal?,
    val deliveryLongitude: BigDecimal?,

    val pickupPointId: Long?,
    val pickupPointTitle: String?,
    val pickupPointAddress: String?,
    val pickupPointLatitude: BigDecimal?,
    val pickupPointLongitude: BigDecimal?,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)