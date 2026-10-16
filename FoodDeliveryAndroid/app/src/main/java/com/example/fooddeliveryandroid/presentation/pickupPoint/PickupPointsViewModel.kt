package com.example.fooddeliveryandroid.presentation.pickupPoint

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.data.repository.PickupPointRepository
import com.example.fooddeliveryandroid.domain.model.PickupPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupPointsViewModel @Inject constructor(
    private val pickupPointRepository: PickupPointRepository,
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<PickupPointUIState>(PickupPointUIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPickupPoints()
    }

    val selectedPickupPoint: StateFlow<PickupPoint?> =
        combine(
            _uiState,
            checkoutPreferencesRepository.selectedPickupPointId
        ) { uiState, selectedPickupPointId ->
            if (uiState !is PickupPointUIState.Success) {
                null
            } else {
                uiState.pickupPoints.firstOrNull {
                    it.id == selectedPickupPointId
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )

    private fun loadPickupPoints() {
        viewModelScope.launch {
            _uiState.value = PickupPointUIState.Loading


            when (val result = pickupPointRepository.getPickupPoints()) {
                is NetworkResult.Success -> {
                    _uiState.value = PickupPointUIState.Success(result.data)
                }
                is NetworkResult.Error -> {
                   _uiState.value = PickupPointUIState.Error("Не удалось загрузить точки самовывоза. Ошибка: ${result.exception.message}")
                }
            }
        }
    }

    fun selectPickupPoint(pickupPoint: PickupPoint) {
        viewModelScope.launch {
            checkoutPreferencesRepository.setSelectedPickupPoint(pickupPoint.id)
        }
    }

    fun clearSelectedPickupPoint() {
        viewModelScope.launch {
            checkoutPreferencesRepository
                .clearSelectedPickupPointId()
        }
    }
}