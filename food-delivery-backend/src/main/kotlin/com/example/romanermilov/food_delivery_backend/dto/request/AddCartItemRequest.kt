package com.example.romanermilov.food_delivery_backend.dto.request
import jakarta.validation.constraints.Positive

data class AddCartItemRequest (
    @field:Positive
    val productId: Long,

)