package com.example.fooddeliveryandroid.data.remote.dto

import com.example.fooddeliveryandroid.domain.model.enums.UserRole
import java.time.LocalDateTime

data class UserResponse (
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val role: UserRole,
    val createdAt: String
)