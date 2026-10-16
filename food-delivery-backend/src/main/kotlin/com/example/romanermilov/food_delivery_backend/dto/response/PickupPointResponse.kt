package com.example.romanermilov.food_delivery_backend.dto.response

import java.math.BigDecimal

data class PickupPointResponse(
    val id: Long,
    val title: String,
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val workingHours: String?
)