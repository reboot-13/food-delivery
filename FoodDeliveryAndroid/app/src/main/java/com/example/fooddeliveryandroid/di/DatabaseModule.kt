package com.example.fooddeliveryandroid.di

import android.content.Context
import androidx.room.Room
import com.example.fooddeliveryandroid.data.local.room.AppDataBase
import com.example.fooddeliveryandroid.data.local.room.dao.CartItemDao
import com.example.fooddeliveryandroid.data.local.room.dao.CategoryDao
import com.example.fooddeliveryandroid.data.local.room.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDataBase::class.java,
            name = "food_delivery.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCartItemDao(
        dataBase: AppDataBase
    ): CartItemDao {
        return dataBase.cartItemDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(
        dataBase: AppDataBase
    ): ProductDao {
        return dataBase.productDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(
        dataBase: AppDataBase
    ): CategoryDao {
        return dataBase.categoriesDao()
    }
}