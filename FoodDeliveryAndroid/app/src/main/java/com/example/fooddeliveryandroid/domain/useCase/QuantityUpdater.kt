package com.example.fooddeliveryandroid.domain.useCase

import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import javax.inject.Inject

class QuantityUpdater @Inject constructor(
    private val cartRepository: CartRepository,
) {
suspend fun updateQuantity(productId: Long, quantity: Int): NetworkResult<Any> {
    if (quantity < 1) {
        return cartRepository.deleteCartItem(productId)
    }

    val request = UpdateCartItemQuantityRequest(
        quantity = quantity
    )
    return cartRepository.updateQuantity(productId, request)
    }
}

