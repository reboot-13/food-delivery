package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.response.PickupPointResponse
import com.example.fooddeliveryandroid.domain.model.PickupPoint

object PickupPointMapper {

    fun responseToModel(response: PickupPointResponse): PickupPoint {
        return PickupPoint(
            id = response.id,
            title = response.title,
            address = response.address,
            latitude = response.latitude,
            longitude = response.longitude,
            workingHours = response.workingHours
        )
    }
}