package com.example.fooddeliveryandroid.domain.model

import java.math.BigDecimal

data class PickupPoint(
    val id: Long,
    val title: String,
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val workingHours: String?
)