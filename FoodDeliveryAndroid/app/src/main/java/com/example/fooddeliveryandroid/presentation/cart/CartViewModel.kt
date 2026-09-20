package com.example.fooddeliveryandroid.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.OrderRepository
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.useCase.QuantityUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CartViewModel @Inject constructor (
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val userSession: UserSession,
    private val quantityUpdater: QuantityUpdater
): ViewModel() {

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
    val createdOrderId: StateFlow<Long?> =
        userSession.lastCreatedOrderId
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val currentOrder = userSession
        .lastCreatedOrderId
        .mapLatest { orderId ->
            if (orderId == null) {
                null
            } else {
                when (val orderResult = orderRepository.getOrderById(orderId)){
                    is NetworkResult.Error -> null

                    is NetworkResult.Success -> orderResult.data
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun clearCreatedOrder() {
        viewModelScope.launch {
            userSession.clearLastCreatedOrderId()
        }
    }

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
            TODO("обработать ошибку синхронизации, показать SnackBar")
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

    fun createOrder(
        cartItems: List<CartItem>
    ) {
        viewModelScope.launch {
            when (val orderResult = orderRepository.createOrder(cartItems)) {
                is NetworkResult.Success -> {
                    clearLocalCart()

                    userSession.saveLastCreatedOrderId(
                        orderResult.data.id
                    )
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private suspend fun clearLocalCart() {
        cartRepository.clearLocalCartItems()
    }
}