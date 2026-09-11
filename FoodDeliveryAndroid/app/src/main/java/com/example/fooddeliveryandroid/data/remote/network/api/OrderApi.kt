package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.OrderResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateOrderRequest
import com.example.fooddeliveryandroid.domain.model.Order
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface OrderApi {
    @GET("orders")
    suspend fun getOrders(): List<Order>

    @GET("orders/{orderId}")
    suspend fun getOrderById(
        @Path("orderId") orderId: Long
    ): Order

    @POST("orders")
    suspend fun createOrder(createOrderRequest: CreateOrderRequest): OrderResponse
}