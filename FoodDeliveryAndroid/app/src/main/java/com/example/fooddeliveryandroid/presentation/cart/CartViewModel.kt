package com.example.fooddeliveryandroid.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.useCase.QuantityUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (
    private val cartRepository: CartRepository,
    private val userSession: UserSession,
    private val quantityUpdater: QuantityUpdater
): ViewModel() {
    private val _uiState = MutableStateFlow<CartUIState>(CartUIState.Loading)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CartUIState> =
        userSession.currentUser
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(CartUIState.Unauthorized)
            } else {
                cartRepository
                    .observeCartItems()
                    .map<List<CartItem>, CartUIState> { cartItems ->
                        CartUIState.Success(cartItems)
                    }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = CartUIState.Loading
        )

    init {
        viewModelScope.launch {
            userSession.currentUser
                .filterNotNull()
                .collect {
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

    fun updateCartItemQuantity(productId: Long, quantity: Int) {
        viewModelScope.launch {
            val updateResult = quantityUpdater.updateQuantity(productId, quantity)
            if (updateResult is NetworkResult.Error) {
                TODO("обработать ошибку, например, SnackBar")
            }
        }
    }

}