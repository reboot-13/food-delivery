package com.example.romanermilov.food_delivery_backend.dto.request

import com.example.romanermilov.food_delivery_backend.entity.enum.OrderType

data class CreateOrderRequest (
    val items: List<CreateOrderItemRequest>,
    val orderType: OrderType,
    val addressId: Long? = null,
    val pickupPointId: Long? = null
)