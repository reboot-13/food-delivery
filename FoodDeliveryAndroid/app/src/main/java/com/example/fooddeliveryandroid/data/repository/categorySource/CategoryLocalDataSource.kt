package com.example.fooddeliveryandroid.data.repository.categorySource

import com.example.fooddeliveryandroid.data.local.entity.CategoryEntity
import com.example.fooddeliveryandroid.data.local.room.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun observeCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.observeCategories()
    }

    suspend fun insertAll(categories: List<CategoryEntity>) {
        categoryDao.insertAll(categories)
    }
}