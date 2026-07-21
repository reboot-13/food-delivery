package com.example.romanermilov.food_delivery_backend.dto.response

data class AuthResponse (
    val user: UserResponse,
    val token: String
)