package com.example.fooddeliveryandroid.data.remote.dto.response

import com.example.fooddeliveryandroid.domain.model.enums.UserRole

data class UserResponse (
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val role: UserRole,
    val createdAt: String,
    val addresses: List<AddressResponse>
)