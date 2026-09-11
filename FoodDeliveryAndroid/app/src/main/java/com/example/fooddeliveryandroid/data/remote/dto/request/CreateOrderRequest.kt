package com.example.fooddeliveryandroid.data.remote.dto.request

data class CreateOrderRequest (
    val items: List<CreateOrderItemRequest>
)