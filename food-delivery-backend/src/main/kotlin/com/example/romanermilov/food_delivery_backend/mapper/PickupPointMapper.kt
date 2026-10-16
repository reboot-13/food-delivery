package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.PickupPointResponse
import com.example.romanermilov.food_delivery_backend.entity.PickupPointEntity
import kotlin.collections.map

object PickupPointMapper {

    fun toResponse(entity: PickupPointEntity): PickupPointResponse {
        return PickupPointResponse(
            id = entity.id!!,
            title = entity.title,
            address = entity.address,
            latitude = entity.latitude,
            longitude = entity.longitude,
            workingHours = entity.workingHours
        )
    }

    fun toResponse(
        entities: List<PickupPointEntity>
    ): List<PickupPointResponse> {
        return entities.map(::toResponse)
    }
}