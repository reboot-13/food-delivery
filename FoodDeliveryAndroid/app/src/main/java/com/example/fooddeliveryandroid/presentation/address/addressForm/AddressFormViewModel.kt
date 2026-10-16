package com.example.fooddeliveryandroid.presentation.address.addressForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateAddressRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.AddressRepository
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddressFormViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val checkoutPreferencesRepository: CheckoutPreferencesRepository,
    private val userSession: UserSession
): ViewModel() {
    private val _uiState = MutableStateFlow(AddressFormUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<CheckoutAddressEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var isInitialized = false

    fun initialize(selection: AddressMapSelection?) {
        if (isInitialized) return
        isInitialized = true

        _uiState.update { state ->
            state.copy(
                mapSelection = selection,
                city = selection?.city.orEmpty(),
                street = selection?.street.orEmpty(),
                house = selection?.house.orEmpty()
            )
        }

    }

    fun createAddress() {
        val state = _uiState.value
        if (!validateAddress()) return

        val selection = _uiState.value.mapSelection

        if (selection != null) {
            // Координаты уже известны из карты.
            val latitude = BigDecimal.valueOf(selection.latitude)
            val longitude = BigDecimal.valueOf(selection.longitude)
        }

            val request = CreateAddressRequest(
            title = state.title.trim().takeIf { it.isNotEmpty() },
            city = state.city.trim(),
            street = state.street.trim(),
            house = state.house.trim(),
            apartment = state.apartment.trim().takeIf { it.isNotEmpty() },
            entrance = state.entrance.trim().takeIf { it.isNotEmpty() },
            floor = state.floor.trim().takeIf { it.isNotEmpty() }?.toShortOrNull(),
            intercom = state.intercom.trim().takeIf { it.isNotEmpty() },
            comment = state.comment.trim().takeIf { it.isNotEmpty() },
            latitude = BigDecimal.valueOf(selection!!.latitude),
            longitude = BigDecimal.valueOf(selection.longitude)
        )

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isSaving = true,
                    error = null
                )
            }
            try {
                when (val result = addressRepository.createAddress(request)) {
                    is NetworkResult.Success -> {
                        val address = result.data
                        userSession.addAddress(address)
                        checkoutPreferencesRepository.setSelectedAddressId(address.id)
                        _events.send(
                            CheckoutAddressEvent.AddressCreated(address)
                        )

                    }
                    is NetworkResult.Error -> {
                        when (result.code) {
                            400 -> _uiState.update { it.copy(error = "Проверьте введённые данные")}
                            else -> _uiState.update { it.copy(error = "Не удалось сохранить адрес") }
                        }
                    }
                }
            } finally {
                _uiState.update { it.copy ( isSaving = false) }
            }

        }
    }

    private fun validateAddress(): Boolean{
        val state = _uiState.value
        if (state.isSaving) return false

        when {


            state.city.isBlank() -> {
                _uiState.update {
                    it.copy(error = "Укажите город")
                }
                return false
            }

            state.street.isBlank() -> {
                _uiState.update {
                    it.copy(error = "Укажите улицу")
                }
                return false
            }

            state.house.isBlank() -> {
                _uiState.update {
                    it.copy(error = "Укажите номер дома")
                }
                return false
            }
            state.floor.isNotBlank() && state.floor.toShortOrNull() == null -> {
                _uiState.update {
                    it.copy(error = "Этаж должен быть целым числом")
                }
                return false
            }
        }
        return true
    }

    fun onTitleChanged(value: String) {
        updateForm { copy(title = value) }
    }

    fun onCityChanged(value: String) {
        updateForm { copy(city = value) }
    }

    fun onStreetChanged(value: String) {
        updateForm { copy(street = value) }
    }

    fun onHouseChanged(value: String) {
        updateForm { copy(house = value) }
    }

    fun onApartmentChanged(value: String) {
        updateForm { copy(apartment = value) }
    }

    fun onEntranceChanged(value: String) {
        updateForm { copy(entrance = value) }
    }

    fun onFloorChanged(value: String) {
        updateForm { copy(floor = value) }
    }

    fun onIntercomChanged(value: String) {
        updateForm { copy(intercom = value) }
    }

    fun onCommentChanged(value: String) {
        updateForm { copy(comment = value) }
    }

    private fun updateForm(update: AddressFormUiState.() -> AddressFormUiState) {
        _uiState.update {state ->
            state.update().copy(error = null)
        }
    }

}