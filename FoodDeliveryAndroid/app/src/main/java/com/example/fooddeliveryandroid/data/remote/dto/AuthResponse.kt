package com.example.fooddeliveryandroid.data.remote.dto

data class AuthResponse (
    val user: UserResponse,
    val token: String
)