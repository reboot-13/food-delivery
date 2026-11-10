package com.example.fooddeliveryandroid.presentation.address.addressForm

import com.example.fooddeliveryandroid.domain.model.Address

sealed interface AddressFormEvent {
    data class AddressFormCreated(
        val address: Address
    ) : AddressFormEvent
}