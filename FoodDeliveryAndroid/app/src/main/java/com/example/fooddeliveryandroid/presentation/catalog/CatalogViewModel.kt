package com.example.fooddeliveryandroid.presentation.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.CatalogRepository
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.data.repository.PickupPointRepository
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.CatalogData
import com.example.fooddeliveryandroid.domain.model.PickupPoint
import com.example.fooddeliveryandroid.domain.model.User
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import com.example.fooddeliveryandroid.domain.useCase.QuantityUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    private val userSession: UserSession,
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository,
    private val pickupPointRepository: PickupPointRepository,
    private val quantityUpdater: QuantityUpdater
) : ViewModel(){
    val uiState: StateFlow<CatalogUIState> =
        catalogRepository
            .observeCatalogData()
            .map<CatalogData, CatalogUIState>{ data ->
                CatalogUIState.Success(data)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = CatalogUIState.Loading
            )

    private val currentUser: StateFlow<User?> = userSession.currentUser

    private val _isAddressesListShow = MutableStateFlow(false)
    val isAddressListShow = _isAddressesListShow.asStateFlow()

    val selectedAddress:  StateFlow<Address?> =
        combine(
            currentUser,
            checkoutPreferencesRepository.selectedAddressId
        ) { user, selectedAddressId ->
            user?.addresses?.firstOrNull { address ->
                address.id == selectedAddressId
            }
        } .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val selectedPickupPoint: StateFlow<PickupPoint?> =
        checkoutPreferencesRepository.selectedPickupPointId
            .mapLatest { pickupPointId ->
                if (pickupPointId == null) null
                else {

                    when (val result = pickupPointRepository.getPickupPoint(pickupPointId)) {
                        is NetworkResult.Success -> result.data
                        is NetworkResult.Error -> null
                    }

                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val orderType: StateFlow<OrderType?> =
        checkoutPreferencesRepository
            .orderType
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    private val _events = MutableSharedFlow<CatalogEvent>()
    val events = _events.asSharedFlow()

    init {
        loadCatalog()
    }

    fun showAddressList() {
        viewModelScope.launch {
            if (currentUser.value == null) {
                _events.emit(CatalogEvent.ShowAuthToChooseAddressSnackBar)
                return@launch
            }
        }
        _isAddressesListShow.value = true
    }

    fun closeAddressList() {

        _isAddressesListShow.value = false

    }

    fun updateQuantity(productId: Long, quantity: Int) {
        viewModelScope.launch {
            val updateResult = quantityUpdater.updateQuantity(productId, quantity)
            if (updateResult is NetworkResult.Error) {
                _events.emit(CatalogEvent.ShowSyncSnackBar)
            }
        }
    }

    fun addProductToCart(productId: Long) {
        if (currentUser.value == null) {
            viewModelScope.launch {
                _events.emit(CatalogEvent.ShowAuthSnackBar)
            }
            return
        }

        when (orderType.value) {

            OrderType.DELIVERY -> {
                if (selectedAddress.value == null){
                    viewModelScope.launch {
                        _events.emit(CatalogEvent.ShowSelectAddressSnackBar)
                    }
                    return
                }
            }

            OrderType.PICKUP ->
                if (selectedPickupPoint.value == null){
                    viewModelScope.launch {
                        _events.emit(CatalogEvent.ShowSelectAddressSnackBar)
                    }
                    return
                }
            else -> {
                viewModelScope.launch {
                    _events.emit(CatalogEvent.ShowSelectAddressSnackBar)
                }
            }
        }

        viewModelScope.launch {
            val request = AddCartItemRequest(productId)
            val result = cartRepository.addCartItem(request)
            if (result is NetworkResult.Error) {
                _events.emit(CatalogEvent.ShowSyncSnackBar)
            }
        }
    }



    fun loadCatalog() {
        viewModelScope.launch {
            val result = catalogRepository.syncCatalog()
            if (result is NetworkResult.Error) {
                _events.emit(CatalogEvent.ShowSyncSnackBar)
            }
        }
    }

    fun showAuthToChooseAddressSnackBar() {
        viewModelScope.launch {
            _events.emit(CatalogEvent.ShowSelectAddressSnackBar)
        }
    }
}
