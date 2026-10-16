package com.example.fooddeliveryandroid.presentation.pickupPoint

import com.example.fooddeliveryandroid.domain.model.PickupPoint

sealed class PickupPointUIState {
    data class Success(val pickupPoints: List<PickupPoint>): PickupPointUIState()
    data object Loading: PickupPointUIState()
    data class Error(val message: String): PickupPointUIState()
}