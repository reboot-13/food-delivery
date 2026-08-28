package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.CategoryApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.data.repository.categorySource.CategoryLocalDataSource
import com.example.fooddeliveryandroid.data.repository.categorySource.CategoryRemoteDataSource
import com.example.fooddeliveryandroid.domain.mapper.CategoryMapper
import com.example.fooddeliveryandroid.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val categoryRemoteDataSource: CategoryRemoteDataSource
) {

    fun observeCategories(): Flow<List<Category>> {
        return categoryLocalDataSource
            .observeCategories()
            .map { entities ->
                entities.map(CategoryMapper::entityToModel)
            }
    }

    suspend fun syncCategories(): NetworkResult<Unit> {
        return safeApiCall {
            val categories = categoryRemoteDataSource.getCategories()

            categoryLocalDataSource.insertAll(
                categories.map(CategoryMapper::responseToEntity)
            )
        }
    }
}