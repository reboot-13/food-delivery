package com.example.fooddeliveryandroid.data.remote.dto.request

import com.example.fooddeliveryandroid.domain.model.enums.OrderType

data class CreateOrderRequest (
    val items: List<CreateOrderItemRequest>,
    val orderType: OrderType,
    val addressId: Long?,
    val pickupPointId: Long?
)