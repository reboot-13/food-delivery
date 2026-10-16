package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.response.PickupPointResponse
import com.example.romanermilov.food_delivery_backend.exception.PickupPointNotFoundException
import com.example.romanermilov.food_delivery_backend.mapper.PickupPointMapper
import com.example.romanermilov.food_delivery_backend.repository.PickupPointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PickupPointService(
    private val pickupPointRepository: PickupPointRepository
) {

    fun getPickupPoints(): List<PickupPointResponse> {
        return PickupPointMapper.toResponse(
            pickupPointRepository.findAllByActiveTrueOrderByTitleAsc()
        )
    }

    fun getPickupPoint(id: Long): PickupPointResponse {
        val pickupPoint = pickupPointRepository
            .findByIdAndActiveTrue(id)
            .orElseThrow {
                PickupPointNotFoundException(id)
            }

        return PickupPointMapper.toResponse(pickupPoint)
    }
}