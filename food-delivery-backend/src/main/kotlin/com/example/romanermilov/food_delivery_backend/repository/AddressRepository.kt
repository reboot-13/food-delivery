package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional


interface AddressRepository: JpaRepository<AddressEntity, Long> {
    fun findAllByUserId(userId: Long): List<AddressEntity>

    fun findByIdAndUserId(
        addressId: Long,
        userId: Long
    ): Optional<AddressEntity>

}