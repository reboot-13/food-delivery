package com.example.romanermilov.food_delivery_backend.repository

import com.example.romanermilov.food_delivery_backend.entity.PickupPointEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PickupPointRepository : JpaRepository<PickupPointEntity, Long> {

    fun findAllByActiveTrueOrderByTitleAsc(): List<PickupPointEntity>

    fun findByIdAndActiveTrue(id: Long): Optional<PickupPointEntity>
}