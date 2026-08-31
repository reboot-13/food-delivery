package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.data.repository.productSource.ProductLocalDataSource
import com.example.fooddeliveryandroid.data.repository.productSource.ProductRemoteDataSource
import com.example.fooddeliveryandroid.domain.mapper.ProductMapper
import com.example.fooddeliveryandroid.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class ProductRepository @Inject constructor(
    private val productLocalDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource
) {

    fun observeProducts(): Flow<List<Product>> {
        return productLocalDataSource.observeProducts().map { productsWithCategory ->
            productsWithCategory.map (
                ProductMapper::productWithCategoryToProduct
            )
        }
    }

    fun observeProductById(productId: Long): Flow<Product>{

        return productLocalDataSource.observeProductById(productId).map { product ->
            ProductMapper.productWithCategoryToProduct(product)
        }

    }

    suspend fun syncProducts(): NetworkResult<Unit> {
        return safeApiCall {
            val products = productRemoteDataSource.getProducts()
            val entities = products.map (
                ProductMapper::responseToEntity
            )
            productLocalDataSource.insertAll(entities)
        }
    }
}