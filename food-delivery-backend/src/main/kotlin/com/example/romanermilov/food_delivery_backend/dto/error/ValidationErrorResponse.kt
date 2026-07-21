package com.example.romanermilov.food_delivery_backend.dto.error

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val errors: List<String>,
    val timestamp: String
)