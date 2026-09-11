package com.example.fooddeliveryandroid.di

import com.example.fooddeliveryandroid.data.remote.network.AuthInterceptor
import com.example.fooddeliveryandroid.data.remote.network.api.CartApi
import com.example.fooddeliveryandroid.data.remote.network.api.CategoryApi
import com.example.fooddeliveryandroid.data.remote.network.api.OrderApi
import com.example.fooddeliveryandroid.data.remote.network.api.ProductApi
import com.example.fooddeliveryandroid.data.remote.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.1.16:8080/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(
        retrofit: Retrofit
    ) : ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(
        retrofit: Retrofit
    ): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }
    @Provides
    @Singleton
    fun provideUserApi(
        retrofit: Retrofit
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApi(
        retrofit: Retrofit
    ) : CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderApi(
        retrofit: Retrofit
    ): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }
}