package com.example.fooddeliveryandroid.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (
    private val cartRepository: CartRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<CartUIState>(CartUIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadCartItems()
        }
    }

    private suspend fun loadCartItems(){
        when (val cartDataResult = cartRepository.getCartItems()) {
            is NetworkResult.Success ->
                _uiState.value = CartUIState.Success(
                    cartDataResult.data
            )
            is NetworkResult.Error ->
                _uiState.value = CartUIState.Error(cartDataResult.exception.message ?: "Неизвестная ошибка")
        }
    }
}