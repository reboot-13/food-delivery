package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Long) : ProductResponse
}