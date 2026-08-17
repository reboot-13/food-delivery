package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.CartApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.CartItemMapper
import com.example.fooddeliveryandroid.domain.model.CartItem
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val cartApi: CartApi
) {
    suspend fun getCartItems(): NetworkResult<List<CartItem>> {
        return safeApiCall {
            val cartItems = cartApi.getCartItems()
            cartItems.map (
                CartItemMapper::responseToModel
                )
           }
    }

    suspend fun addCartItem(request: AddCartItemRequest): NetworkResult<CartItem> {
        return safeApiCall {
            val cartItem = cartApi.createCartItem(request)
            CartItemMapper.responseToModel(cartItem)
        }
    }

    suspend fun updateQuantity(productId: Long, request: UpdateCartItemQuantityRequest): NetworkResult<CartItem> {
        return safeApiCall {
            val cartItem = cartApi.updateQuantity(productId, request)
            CartItemMapper.responseToModel(cartItem)
        }
    }

    suspend fun deleteCartItem(productId: Long): NetworkResult<Unit> {
        return safeApiCall {
            cartApi.deleteCartItem(productId)
        }
    }
}