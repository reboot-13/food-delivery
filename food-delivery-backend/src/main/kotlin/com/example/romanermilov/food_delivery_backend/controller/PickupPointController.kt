package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.response.PickupPointResponse
import com.example.romanermilov.food_delivery_backend.service.PickupPointService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/pickup-points")
class PickupPointController(
    private val pickupPointService: PickupPointService
) {

    @GetMapping
    fun getPickupPoints(): List<PickupPointResponse> {
        return pickupPointService.getPickupPoints()
    }

    @GetMapping("/{id}")
    fun getPickupPoint(
        @PathVariable id: Long
    ): PickupPointResponse {
        return pickupPointService.getPickupPoint(id)
    }
}