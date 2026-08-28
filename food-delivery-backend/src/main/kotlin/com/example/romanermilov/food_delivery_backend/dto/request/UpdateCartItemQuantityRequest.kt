package com.example.romanermilov.food_delivery_backend.dto.request

import jakarta.validation.constraints.Positive

data class UpdateCartItemQuantityRequest (
    @field:Positive
    val quantity: Short
)