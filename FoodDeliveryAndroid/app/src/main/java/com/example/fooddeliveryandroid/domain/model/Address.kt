package com.example.fooddeliveryandroid.domain.model

import java.math.BigDecimal

data class Address(
    val id: Long,
    val title: String?,
    val city: String,
    val street: String,
    val house: String,
    val apartment: String?,
    val entrance: String?,
    val floor: Short?,
    val intercom: String?,
    val comment: String?,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)