package com.example.fooddeliveryandroid.data.repository.productSource

import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import com.example.fooddeliveryandroid.data.local.room.dao.ProductDao
import com.example.fooddeliveryandroid.data.local.room.relation.ProductWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val productDao: ProductDao
) {

    fun observeProducts(): Flow<List<ProductWithCategory>> {
        return productDao.observeProducts()
    }

    suspend fun insertAll(products: List<ProductEntity>) {
        productDao.insertAll(products)
    }

    fun observeProductById(productId: Long): Flow<ProductWithCategory> {
        return productDao.observeProductById(productId)
    }
}