package com.example.fooddeliveryandroid.data.repository.cartSource

import com.example.fooddeliveryandroid.data.remote.dto.CartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import com.example.fooddeliveryandroid.data.remote.network.api.CartApi
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val cartApi: CartApi
) {

    suspend fun getCartItems(): List<CartItemResponse> {
        return cartApi.getCartItems()
    }

    suspend fun addCartItem(
        request: AddCartItemRequest
    ): CartItemResponse {
        return cartApi.addCartItem(request)
    }

    suspend fun updateQuantity(
        productId: Long,
        request: UpdateCartItemQuantityRequest
    ): CartItemResponse {
        return cartApi.updateQuantity(productId, request)
    }

    suspend fun deleteCartItem(
        productId: Long
    ) {
        cartApi.deleteCartItem(productId)
    }
}