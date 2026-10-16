package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.AddressResponse
import com.example.romanermilov.food_delivery_backend.dto.response.UserResponse
import com.example.romanermilov.food_delivery_backend.entity.UserEntity

object UserMapper {
    fun toResponse(
        user: UserEntity,
        addresses: List<AddressResponse> = emptyList()
    ): UserResponse {
        return UserResponse(
            id = user.id!!,
            name = user.name,
            phoneNumber = user.phoneNumber,
            role = user.role,
            createdAt = user.createdAt,
            addresses = addresses)
    }
}