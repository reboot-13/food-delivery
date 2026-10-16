package com.example.fooddeliveryandroid.domain.model

data class GeocodedAddress(
    val formattedAddress: String,
    val city: String?,
    val street: String?,
    val house: String?
)