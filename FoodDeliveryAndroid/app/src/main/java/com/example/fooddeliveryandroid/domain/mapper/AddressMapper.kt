package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.response.AddressResponse
import com.example.fooddeliveryandroid.domain.model.Address

object AddressMapper {
    fun responseToModel(addressResponse: AddressResponse): Address {
        return with(addressResponse) {
            Address(
                id = id,
                title = title,
                city = city,
                street = street,
                house = house,
                apartment = apartment,
                entrance = entrance,
                floor = floor,
                intercom = intercom,
                comment = comment,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}