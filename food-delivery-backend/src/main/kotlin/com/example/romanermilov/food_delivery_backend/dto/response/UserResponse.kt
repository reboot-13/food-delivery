package com.example.romanermilov.food_delivery_backend.dto.response

import com.example.romanermilov.food_delivery_backend.entity.enum.UserRole

data class UserResponse (
    val id: Long,
    val phoneNumber: String,
    val role: UserRole
)