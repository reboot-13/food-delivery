package com.example.fooddeliveryandroid.presentation.orderTypeSelector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderTypeSelectorViewModel @Inject constructor(
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository
): ViewModel() {

    val orderType: StateFlow<OrderType> =
        checkoutPreferencesRepository.orderType
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                OrderType.DELIVERY
            )

    fun setOrderType (orderType: OrderType) {
        viewModelScope.launch {
            checkoutPreferencesRepository.setOrderType(orderType)
        }
    }
}