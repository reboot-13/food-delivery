package com.example.fooddeliveryandroid.data.repository.productSource

import com.example.fooddeliveryandroid.data.remote.dto.ProductResponse
import com.example.fooddeliveryandroid.data.remote.dto.ShortProductResponse
import com.example.fooddeliveryandroid.data.remote.network.api.ProductApi
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val productApi: ProductApi
) {

    suspend fun getProducts(): List<ShortProductResponse> {
        return productApi.getProducts()
    }


    suspend fun getProductById(productId: Long) : ProductResponse {
        return productApi.getProductById(productId)
    }
}