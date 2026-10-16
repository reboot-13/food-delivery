package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.response.AuthResponse
import com.example.fooddeliveryandroid.data.remote.dto.response.UserResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.AuthRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.RegisterRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {
    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("users/auth")
    suspend fun auth(@Body request: AuthRequest) : AuthResponse

    @GET("users/me")
    suspend fun getCurrentUser(): UserResponse

    @PUT("users/me")
    suspend fun updateCurrentUser(
        @Body request: UpdateProfileRequest
    ): UserResponse
}