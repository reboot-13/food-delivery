package com.example.fooddeliveryandroid.domain.model

import java.io.Serializable


data class AddressMapSelection(
    val latitude: Double,
    val longitude: Double,
    val formattedAddress: String? = null,
    val city: String? = null,
    val street: String? = null,
    val house: String? = null
): Serializable