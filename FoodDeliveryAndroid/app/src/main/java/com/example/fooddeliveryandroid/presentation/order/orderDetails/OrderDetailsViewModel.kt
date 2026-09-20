package com.example.fooddeliveryandroid.presentation.order.orderDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.repository.OrderRepository
import com.example.fooddeliveryandroid.domain.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
): ViewModel(){
    private val orderId = MutableStateFlow<Long?>(null)

    val order: StateFlow<Order?> = orderId
        .flatMapLatest { orderId ->
            if (orderId == null) {
                flowOf(null )
            } else {
                orderRepository.observeOrder(orderId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setOrderId (id: Long){
        orderId.value = id
    }
}