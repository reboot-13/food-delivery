package com.example.romanermilov.food_delivery_backend.dto.response

import com.example.romanermilov.food_delivery_backend.entity.enum.UserRole
import java.time.LocalDateTime

data class  UserResponse (
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val role: UserRole,
    val createdAt: LocalDateTime
)