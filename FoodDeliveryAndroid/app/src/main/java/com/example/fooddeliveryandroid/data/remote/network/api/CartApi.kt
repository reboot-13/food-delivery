package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.CartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApi {
    @GET("cart")
    suspend fun getCartItems(): List<CartItemResponse>

    @POST("items")
    suspend fun createCartItem(@Body addCartItemRequest: AddCartItemRequest): CartItemResponse

    @DELETE("items/{id}")
    suspend fun deleteCartItem(@Path("id") productId: Long)

    @PATCH ("items/{id}")
    suspend fun updateQuantity(
        @Path("id") productId: Long,
        @Body updateCartItemQuantityRequest: UpdateCartItemQuantityRequest
    ): CartItemResponse
}