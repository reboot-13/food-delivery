package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.dto.request.CreateOrderRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.OrderApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.OrderMapper
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.model.Order
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderApi: OrderApi
) {
    suspend fun getOrders(): NetworkResult<List<Order>> {
        return safeApiCall {
            orderApi.getOrders().map { orderResponse ->
                OrderMapper.responseToModel(orderResponse)
            }.asReversed()
        }
    }


    suspend fun getOrderById(id: Long): NetworkResult<Order> {
        return safeApiCall {
            val response = orderApi.getOrderById(id)
            OrderMapper.responseToModel(response)
        }
    }

    suspend fun createOrder(cartItems: List<CartItem>): NetworkResult<Order>{
        return safeApiCall {
            val request = CreateOrderRequest(
                items = cartItems.map { cartItem ->
                    OrderMapper.cartItemToOrderItemRequest(cartItem)
               }
            )
            val response = orderApi.createOrder(request)
            OrderMapper.responseToModel(response)
        }
    }
}