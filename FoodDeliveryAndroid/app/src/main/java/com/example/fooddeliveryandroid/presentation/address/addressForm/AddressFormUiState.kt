package com.example.fooddeliveryandroid.presentation.address.addressForm

import com.example.fooddeliveryandroid.domain.model.AddressMapSelection

data class AddressFormUiState(
    val mapSelection: AddressMapSelection? = null,

    val title: String = "",
    val city: String = "",
    val street: String = "",
    val house: String = "",
    val apartment: String = "",
    val entrance: String = "",
    val floor: String = "",
    val intercom: String = "",
    val comment: String = "",

    val isSaving: Boolean = false,
    val error: String? = null
)