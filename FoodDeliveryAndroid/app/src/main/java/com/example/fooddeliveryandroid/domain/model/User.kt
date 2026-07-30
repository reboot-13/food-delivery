package com.example.fooddeliveryandroid.domain.model

import com.example.fooddeliveryandroid.domain.model.enums.UserRole
import java.time.LocalDateTime

data class User (
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val role: UserRole = UserRole.USER,
    val createdAt: LocalDateTime
)