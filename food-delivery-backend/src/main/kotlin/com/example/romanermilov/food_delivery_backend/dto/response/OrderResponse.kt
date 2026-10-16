package com.example.romanermilov.food_delivery_backend.dto.response
import com.example.romanermilov.food_delivery_backend.entity.enum.OrderStatus
import com.example.romanermilov.food_delivery_backend.entity.enum.OrderType
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse (
    val id: Long,
    val userId: Long,
    val items: List<OrderItemResponse>,
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
    )