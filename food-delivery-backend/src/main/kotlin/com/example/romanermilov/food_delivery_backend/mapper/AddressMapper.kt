package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.AddressResponse
import com.example.romanermilov.food_delivery_backend.entity.AddressEntity

object AddressMapper {

    fun toResponse(address: AddressEntity): AddressResponse {
        return AddressResponse(
            id = requireNotNull(address.id),
            title = address.title,
            city = address.city,
            street = address.street,
            house = address.house,
            apartment = address.apartment,
            entrance = address.entrance,
            floor = address.floor,
            intercom = address.intercom,
            comment = address.comment,
            latitude = address.latitude,
            longitude = address.longitude
        )
    }
}