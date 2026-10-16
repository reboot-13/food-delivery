package com.example.fooddeliveryandroid.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.AddressRepository
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository,
    private val userSession: UserSession,
) : ViewModel() {

    val addresses: StateFlow<List<Address>> =
        userSession.currentUser
            .map { user ->
                user?.addresses ?: emptyList()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val selectedAddress:  StateFlow<Address?> =
        combine(
            addresses,
            checkoutPreferencesRepository.selectedAddressId
        ) { addresses, selectedAddressId ->
            addresses.firstOrNull { address ->
                address.id == selectedAddressId
            }
        } .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _deletingAddressId = MutableStateFlow<Long?>(null)
    val deletingAddressId: StateFlow<Long?> = _deletingAddressId.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun selectAddress(address: Address) {
        viewModelScope.launch {
            checkoutPreferencesRepository.setSelectedAddressId(address.id)
        }
    }

    fun deleteAddress(addressId: Long) {
        if (_deletingAddressId.value != null) {
            return
        }

        viewModelScope.launch {
            _deletingAddressId.value = addressId
            clearError()

            when (val result = addressRepository.deleteAddress(addressId)) {
                is NetworkResult.Success -> {
                    userSession.removeAddress(addressId)
                    if (selectedAddress.value?.id == addressId) {
                        checkoutPreferencesRepository.clearSelectedAddressId()
                    }

                }

                is NetworkResult.Error -> {
                    _error.value = "Не удалось удалить адрес"
                }
            }

            _deletingAddressId.value = null
        }
    }

    fun clearError() {
        _error.value = null
    }
}