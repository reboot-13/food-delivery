package com.example.fooddeliveryandroid.presentation.address.addAddress

import com.example.fooddeliveryandroid.domain.model.AddressMapSelection

data class AddAddressUIState (
    val selection: AddressMapSelection = AddressMapSelection (
        longitude = AddressMapDefaults.LONGITUDE,
        latitude = AddressMapDefaults.LATITUDE,
    ),
    val isGeocoding: Boolean = false,
    val error: String? = null,
)

object AddressMapDefaults {
    const val LATITUDE = 55.751225
    const val LONGITUDE = 37.629540
    const val ZOOM = 15f
}