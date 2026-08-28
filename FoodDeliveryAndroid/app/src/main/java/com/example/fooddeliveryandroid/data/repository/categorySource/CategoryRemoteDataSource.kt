package com.example.fooddeliveryandroid.data.repository.categorySource

import com.example.fooddeliveryandroid.data.remote.dto.CategoryShortResponse
import com.example.fooddeliveryandroid.data.remote.network.api.CategoryApi
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val categoryApi: CategoryApi
) {
    suspend fun getCategories(): List<CategoryShortResponse> {
        return categoryApi.getAllCategories()
    }
}