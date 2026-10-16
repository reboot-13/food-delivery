package com.example.fooddeliveryandroid.domain.useCase

import com.example.fooddeliveryandroid.domain.model.Address

fun buildAddressText(address: Address): String {
    return buildString {
        append(address.city)
        append(", ")
        append(address.street)
        append(", ")
        append(address.house)

        address.apartment?.takeIf { it.isNotBlank() }?.let {
            append(", кв. ")
            append(it)
        }
    }
}