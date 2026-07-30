package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<UserEntity, Long>{
    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}