package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.UserResponse
import com.example.fooddeliveryandroid.domain.model.User
import java.time.LocalDateTime

object UserMapper {
    fun responseToModel(response: UserResponse): User {
        return User(
            id = response.id,
            name = response.name,
            phoneNumber = response.phoneNumber,
            role = response.role,
            createdAt = LocalDateTime.parse(response.createdAt)
        )
    }
}