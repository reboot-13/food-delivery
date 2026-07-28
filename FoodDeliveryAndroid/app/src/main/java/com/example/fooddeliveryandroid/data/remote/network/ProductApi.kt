package com.example.fooddeliveryandroid.data.remote.network

import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import com.example.fooddeliveryandroid.data.remote.dto.ShortProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ShortProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Long) : ProductResponse
}