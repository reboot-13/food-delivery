package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.api.CategoryApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.CategoryMapper
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi
) {
    suspend fun getAllCategories() = safeApiCall {  categoryApi
        .getAllCategories()
        .map(CategoryMapper::responseToModel)
    }
}