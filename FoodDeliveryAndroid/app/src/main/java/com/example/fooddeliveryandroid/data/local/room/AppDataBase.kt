package com.example.fooddeliveryandroid.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.local.entity.CategoryEntity
import com.example.fooddeliveryandroid.data.local.entity.ProductEntity
import com.example.fooddeliveryandroid.data.local.room.converter.BigDecimalConverter
import com.example.fooddeliveryandroid.data.local.room.dao.CartItemDao
import com.example.fooddeliveryandroid.data.local.room.dao.CategoryDao
import com.example.fooddeliveryandroid.data.local.room.dao.ProductDao

@Database(
    entities = [
        CartItemEntity::class,
        ProductEntity::class,
        CategoryEntity::class],
    version = 1
)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDataBase(): RoomDatabase() {
    abstract fun cartItemDao(): CartItemDao
    abstract fun productDao(): ProductDao

    abstract fun categoriesDao(): CategoryDao
}
