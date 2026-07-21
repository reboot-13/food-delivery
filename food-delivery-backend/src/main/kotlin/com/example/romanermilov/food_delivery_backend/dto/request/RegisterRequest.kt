package com.example.romanermilov.food_delivery_backend.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest (
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{10,14}$",
        message = "Invalid phone number format")
    val phoneNumber: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 64, message = "Password must contain from 8 characters")
    val password: String,
)