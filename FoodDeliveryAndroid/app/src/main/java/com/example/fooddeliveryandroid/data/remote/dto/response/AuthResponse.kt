package com.example.fooddeliveryandroid.data.remote.dto.response

data class AuthResponse (
    val user: UserResponse,
    val token: String
)