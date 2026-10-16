package com.example.fooddeliveryandroid.data.remote.dto.response

data class CartItemResponse (
    val product: ProductCartItemResponse,
    val quantity: Int,
)