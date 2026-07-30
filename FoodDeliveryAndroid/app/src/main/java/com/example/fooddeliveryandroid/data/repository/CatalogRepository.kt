package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.domain.model.CatalogData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    suspend fun getCatalogData (): NetworkResult<CatalogData> {
        return coroutineScope {
            val products = async {
                productRepository.getAllProducts()
            }
            val categories = async {
                categoryRepository.getAllCategories()
            }
            val productsListResult = products.await()
            val categoriesListResult = categories.await()
            if (productsListResult is NetworkResult.Error) {
                return@coroutineScope productsListResult
            }
            if (categoriesListResult is NetworkResult.Error) {
                return@coroutineScope categoriesListResult
            }
            productsListResult as NetworkResult.Success
            categoriesListResult as NetworkResult.Success

            NetworkResult.Success(
                CatalogData(
                    categoriesListResult.data,
                    productsListResult.data
                )
            )
        }
    }
}