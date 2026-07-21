package com.example.romanermilov.food_delivery_backend.dto.error

data class ErrorResponse (
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: String
)