package com.example.fooddeliveryandroid.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateCartItemQuantityRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.domain.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (
    private val cartRepository: CartRepository,
    userSession: UserSession
): ViewModel() {
    private val _uiState = MutableStateFlow<CartUIState>(CartUIState.Loading)
    val uiState: StateFlow<CartUIState> = cartRepository
        .observeCartItems()
        .map<List<CartItem>, CartUIState> { cartItems ->
            CartUIState.Success(cartItems)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = CartUIState.Loading
        )

    init {
        viewModelScope.launch {
            if (userSession.currentUser.value == null) {
                _uiState.value = CartUIState.Unauthorized
            } else {
                loadCartItems()
            }
        }
    }

    private suspend fun loadCartItems(){
        val cartDataResult = cartRepository.syncCart()
        if (cartDataResult is NetworkResult.Error) {
            TODO("обработать ошибку синхронизации")
        }
    }

    private fun updateQuantityInState(productId: Long, updatedItem: CartItem) {
        val currentState = _uiState.value
        if (currentState !is CartUIState.Success) return
        val updatedItems = currentState.cartItems.map { cartItem ->
            if (cartItem.product.id == productId) {
                cartItem.copy(quantity = updatedItem.quantity)
            } else
                cartItem
        }
        _uiState.value = CartUIState.Success(updatedItems)
    }

    private fun removeItemInState(productId: Long) {
        val currentState = _uiState.value
        if (currentState !is CartUIState.Success) return

        val updatedItems = currentState.cartItems.filter { cartItem ->
            cartItem.product.id != productId
        }

        _uiState.value = CartUIState.Success(updatedItems)
    }

    fun updateCartItemQuantity(productId: Long, quantity: Int) {
        viewModelScope.launch {
            if (quantity < 1) {
                when (cartRepository.deleteCartItem(productId)) {
                    is NetworkResult.Success ->
                        removeItemInState(productId)

                    is NetworkResult.Error ->
                        _uiState.value = CartUIState.Error("Не удалось удалить товар из корзины")
                }
                return@launch
            }

            val request = UpdateCartItemQuantityRequest(
                quantity = quantity
            )
            when (val updateResult = cartRepository.updateQuantity(productId, request)) {
                is NetworkResult.Success -> {
                    updateQuantityInState(productId, updateResult.data)
                }

                is NetworkResult.Error ->
                    _uiState.value = CartUIState.Error("Не удалось изменить количество")
            }
        }
    }
}