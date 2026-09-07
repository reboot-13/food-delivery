package com.example.fooddeliveryandroid.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.CatalogRepository
import com.example.fooddeliveryandroid.domain.model.CatalogData
import com.example.fooddeliveryandroid.domain.useCase.QuantityUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    private val userSession: UserSession,
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

    private val isUserAuthorized = userSession.currentUser.value

    private val _events = MutableSharedFlow<CatalogEvent>()
    val events = _events.asSharedFlow()

    init {
        loadCatalog()
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
        if (isUserAuthorized == null) {
            viewModelScope.launch {
                _events.emit(CatalogEvent.ShowAuthSnackBar)
            }
            return
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
}
