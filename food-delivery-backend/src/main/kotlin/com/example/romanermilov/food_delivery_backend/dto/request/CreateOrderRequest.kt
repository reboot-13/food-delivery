package com.example.romanermilov.food_delivery_backend.dto.request

import com.example.romanermilov.food_delivery_backend.dto.response.CreateOrderItemRequest

data class CreateOrderRequest (
    val items: List<CreateOrderItemRequest>
)