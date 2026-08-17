package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.CartItemResponse
import com.example.romanermilov.food_delivery_backend.dto.response.ProductCartItemResponse
import com.example.romanermilov.food_delivery_backend.entity.CartItemEntity

object CartItemMapper {
    fun toResponse(cartItem: CartItemEntity): CartItemResponse {
        return CartItemResponse(
            product = ProductCartItemResponse(
                id = cartItem.product.id!!,
                price = cartItem.product.price,
                name = cartItem.product.name,
                available = cartItem.product.available,
                imageUrl = cartItem.product.imageUrl,
            ),
            quantity = cartItem.quantity,
        )
    }
}