package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.data.repository.cartSource.CartLocalDataSource
import com.example.fooddeliveryandroid.data.repository.cartSource.CartRemoteDataSource
import com.example.fooddeliveryandroid.domain.mapper.CartItemMapper
import com.example.fooddeliveryandroid.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val cartLocalDataSource: CartLocalDataSource,
    private val cartRemoteDataSource: CartRemoteDataSource
) {

    fun observeCartItems(): Flow<List<CartItem>> {
        return cartLocalDataSource
            .observeCartItems()
            .map { items ->
                items.map (CartItemMapper::cartItemWithProductToModel)
            }

    }

    suspend fun syncCart(): NetworkResult<Unit> {
        return safeApiCall {
            val cartItems = cartRemoteDataSource.getCartItems()
            cartLocalDataSource.clear()
            cartLocalDataSource.insertAll(
                cartItems.map (
                    CartItemMapper::responseToEntity
                )
            )
        }
    }

    suspend fun addCartItem(request: AddCartItemRequest): NetworkResult<CartItem> {
        return safeApiCall {
            val cartItem = cartRemoteDataSource.addCartItem(request)
            CartItemMapper.responseToModel(cartItem)
        }
    }

    suspend fun updateQuantity(productId: Long, request: UpdateCartItemQuantityRequest): NetworkResult<CartItem> {
        return safeApiCall {
            val cartItem = cartRemoteDataSource.updateQuantity(productId, request)
            CartItemMapper.responseToModel(cartItem)
        }
    }

    suspend fun deleteCartItem(productId: Long): NetworkResult<Unit> {
        return safeApiCall {
            cartRemoteDataSource.deleteCartItem(productId)
        }
    }
}