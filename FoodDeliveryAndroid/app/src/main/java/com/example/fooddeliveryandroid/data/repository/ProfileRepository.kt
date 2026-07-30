package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.dto.AuthResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.AuthRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.RegisterRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.UserApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.datastore.SessionManager
import com.example.fooddeliveryandroid.domain.mapper.UserMapper
import com.example.fooddeliveryandroid.domain.model.User
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userApi: UserApi,
    private val sessionManager: SessionManager
) {
    suspend fun register(registerRequest: RegisterRequest): NetworkResult<AuthResponse> {
        return safeApiCall {
            val authResponse = userApi.register(registerRequest)
            sessionManager.saveToken(authResponse.token)
            authResponse
        }
    }

    suspend fun auth(authRequest: AuthRequest): NetworkResult<AuthResponse> {
        return safeApiCall {
            val authResponse = userApi.auth(authRequest)
            sessionManager.saveToken(authResponse.token)
            authResponse
        }
    }

    suspend fun getCurrentUser(): NetworkResult<User> {
        return safeApiCall {
            val userResponse = userApi.getCurrentUser()
            UserMapper.responseToModel(userResponse)
        }
    }
}