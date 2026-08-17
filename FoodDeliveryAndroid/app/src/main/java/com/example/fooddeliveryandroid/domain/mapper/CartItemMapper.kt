package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.CartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.ProductCartItemResponse
import com.example.fooddeliveryandroid.domain.model.CartItem

object CartItemMapper {
    fun responseToModel (response: CartItemResponse): CartItem {
        val product = ProductCartItemResponse(
            id = response.product.id,
            name = response.product.name,
            price = response.product.price,
            imageUrl = response.product.imageUrl,
            available = response.product.available
        )
        return CartItem(
            product = ProductMapper.responseToModel(product),
            quantity = response.quantity
        )
    }
}