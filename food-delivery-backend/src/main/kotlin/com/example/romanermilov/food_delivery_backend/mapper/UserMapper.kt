package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.UserResponse
import com.example.romanermilov.food_delivery_backend.entity.UserEntity

object UserMapper {
    fun toResponse(user: UserEntity): UserResponse {
        return UserResponse(user.id!!, user.phoneNumber, user.role)
    }
}