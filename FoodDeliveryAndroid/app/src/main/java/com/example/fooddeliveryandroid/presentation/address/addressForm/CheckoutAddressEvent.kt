package com.example.fooddeliveryandroid.presentation.address.addressForm

import com.example.fooddeliveryandroid.domain.model.Address

sealed interface CheckoutAddressEvent {
    data class AddressCreated(
        val address: Address
    ) : CheckoutAddressEvent
}