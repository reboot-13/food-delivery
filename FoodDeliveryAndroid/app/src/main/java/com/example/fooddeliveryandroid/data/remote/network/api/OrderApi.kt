package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.OrderResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateOrderRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface OrderApi {
    @GET("orders")
    suspend fun getOrders(): List<OrderResponse>

    @GET("orders/{orderId}")
    suspend fun getOrderById(
        @Path("orderId") orderId: Long
    ): OrderResponse

    @POST("orders")
    suspend fun createOrder(@Body createOrderRequest: CreateOrderRequest): OrderResponse
}