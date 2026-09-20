package com.example.fooddeliveryandroid.presentation.order.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<OrderUIState>(OrderUIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            when(val result = orderRepository.getOrders()) {
                is NetworkResult.Success -> {
                    _uiState.value = OrderUIState.Success(orders = result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = OrderUIState.Error(message = result.exception.message ?: "Ошибка загрузки данных")
                }
            }
        }
    }



}