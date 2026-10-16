package com.example.fooddeliveryandroid.data.remote.dto.request

import java.math.BigDecimal

data class CreateAddressRequest(
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