package com.example.romanermilov.food_delivery_backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "pickup_points")
class PickupPointEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val title: String,

    @Column(nullable = false, length = 255)
    val address: String,

    @Column(nullable = false, precision = 9, scale = 6)
    val latitude: BigDecimal,

    @Column(nullable = false, precision = 9, scale = 6)
    val longitude: BigDecimal,

    @Column(name = "working_hours", length = 100)
    val workingHours: String? = null,

    @Column(nullable = false)
    val active: Boolean = true
)