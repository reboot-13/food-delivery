package com.example.romanermilov.food_delivery_backend.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class AuthRequest(
    @field:NotBlank("Phone number is required")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{10,14}$",
        message = "Invalid phone number format")
    val phoneNumber: String,

    @field:NotBlank("Password is required")
    val password: String
)