package com.example.fooddeliveryandroid.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import com.example.fooddeliveryandroid.data.local.room.relation.ProductWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun observeProducts(): Flow<List<ProductWithCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query ("SELECT * FROM products WHERE id = :productId")
    fun observeProductById(productId: Long): Flow<ProductWithCategory>

}