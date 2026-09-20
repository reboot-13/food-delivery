package com.example.fooddeliveryandroid.presentation.order.orderDetails

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
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
): ViewModel(){
    private val _uiState = MutableStateFlow<OrderDetailsUIState>(OrderDetailsUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadOrder(orderId: Long) {
        viewModelScope.launch {
            when(val orderResult = orderRepository.getOrderById(orderId)) {
                is NetworkResult.Success -> {
                    _uiState.value = OrderDetailsUIState.Success(orderResult.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = OrderDetailsUIState.Error("Не удалось загрузить заказ ${orderResult.exception.message}")
                }
            }
        }
    }
}