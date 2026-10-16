package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.response.CategoryShortResponse
import retrofit2.http.GET

interface CategoryApi {
    @GET ("categories")
    suspend fun getAllCategories(): List<CategoryShortResponse>
}