package com.example.fooddeliveryandroid.presentation.cart

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.data.repository.OrderRepository
import com.example.fooddeliveryandroid.data.repository.PickupPointRepository
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.model.Order
import com.example.fooddeliveryandroid.domain.model.PickupPoint
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import com.example.fooddeliveryandroid.domain.useCase.QuantityUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CartViewModel @Inject constructor (
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val userSession: UserSession,
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository,
    private val pickupPointRepository: PickupPointRepository,
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

    val orderType: StateFlow<OrderType> =
        checkoutPreferencesRepository
            .orderType
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                OrderType.DELIVERY
            )

    val selectedAddressId: StateFlow<Long?> =
        checkoutPreferencesRepository.selectedAddressId
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val selectedAddress: StateFlow<Address?> =
        combine(
            checkoutPreferencesRepository.selectedAddressId,
            userSession.currentUser
        ) { selectedAddressId, user ->
            user?.addresses?.firstOrNull {
                it.id == selectedAddressId
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val selectedPickupPointId: StateFlow<Long?> =
        checkoutPreferencesRepository.selectedPickupPointId
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val selectedPickupPoint: StateFlow<PickupPoint?> =
        checkoutPreferencesRepository.selectedPickupPointId
            .flatMapLatest { selectedPickupPointId ->
                if (selectedPickupPointId == null)
                    flowOf<PickupPoint?>(null)
                else {
                    flow <PickupPoint?> {
                        when (val result = pickupPointRepository.getPickupPoint(selectedPickupPointId)) {
                            is NetworkResult.Success -> {
                                emit(result.data)
                            }
                            is NetworkResult.Error ->{
                                emit(null)
                            }
                        }
                    }
                }
            } .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val currentOrder: StateFlow<Order?> = userSession
        .lastCreatedOrderId
        .flatMapLatest { orderId ->
            if (orderId == null) {
                flowOf(null)
            } else {
                orderRepository.observeOrder(orderId)
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
        cartItems: List<CartItem>,
    ) {
        viewModelScope.launch {

            val orderType =
                checkoutPreferencesRepository.orderType.first()

            val addressId =
                checkoutPreferencesRepository.selectedAddressId.first()

            val pickupPointId =
                checkoutPreferencesRepository.selectedPickupPointId.first()

            if (!isDestinationValid(
                orderType = orderType,
                addressId = addressId,
                pickupPointId = pickupPointId
            )) {
//             "Добавить SnackBar Выберите адрес / Выберите пункт самовывоза"
                return@launch
            }

            when (val orderResult = orderRepository.createOrder(
                cartItems = cartItems,
                orderType = orderType,
                addressId = addressId,
                pickupPointId = pickupPointId)) {
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

    private fun isDestinationValid(
        orderType: OrderType,
        addressId: Long?,
        pickupPointId: Long?
    ): Boolean {
        return when (orderType) {


            OrderType.DELIVERY ->{
                addressId != null
            }


            OrderType.PICKUP ->
                pickupPointId != null

        }
    }
}