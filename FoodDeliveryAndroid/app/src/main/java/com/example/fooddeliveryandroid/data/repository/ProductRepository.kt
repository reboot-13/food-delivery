package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.ProductApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.ProductMapper
import com.example.fooddeliveryandroid.domain.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi
) {
    suspend fun getAllProducts(): NetworkResult<List<Product>>  =
        safeApiCall {
            productApi
                .getProducts()
                .map (
                    ProductMapper::responseToModel
                )
        }


    suspend fun getProductById(productId: Long): NetworkResult<Product> =
        safeApiCall {
            ProductMapper.responseToModel(
                productApi.getProductById(productId)
            )
        }
}