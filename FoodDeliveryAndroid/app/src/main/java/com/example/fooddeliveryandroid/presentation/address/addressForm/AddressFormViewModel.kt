package com.example.fooddeliveryandroid.presentation.address.addressForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateAddressRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.AddressRepository
import com.example.fooddeliveryandroid.data.repository.CheckoutPreferencesRepository
import com.example.fooddeliveryandroid.domain.geocoder.YandexGeocoder
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.example.fooddeliveryandroid.domain.model.DeliveryCities
import com.example.fooddeliveryandroid.domain.model.DeliveryCity
import com.yandex.mapkit.search.SuggestItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val geocoder: YandexGeocoder,
    private val userSession: UserSession
): ViewModel() {
    private val _uiState = MutableStateFlow(AddressFormUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AddressFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var isInitialized = false
    private var suggestingJob: Job? = null
    private var resolvingJob: Job? = null

    fun initialize(selection: AddressMapSelection?) {
        if (isInitialized) return
        isInitialized = true

        _uiState.update { state ->
            if (selection == null) state
            else{
                val addressText = listOfNotNull(selection.street, selection.house)
                    .map {
                        it.trim()
                    }
                    .filter { it.isNotEmpty() }
                    .joinToString(", ")

                val availableCity = DeliveryCities.values()
                    .firstOrNull{ it.name == selection.city }
                    ?.name

                state.copy(
                    mapSelection = selection,
                    addressQuery = addressText.ifBlank {
                        selection.formattedAddress.orEmpty()
                    },
                    suggestedAddresses = emptyList(),
                    title = state.title.ifBlank {
                        state.title
                    },
                    city = availableCity ?: state.city,
                    street = selection.street.orEmpty(),
                    house = selection.house.orEmpty(),
                    error = null
                )
            }
        }
    }

    fun onAddressQueryChanged(query: String) {
        suggestingJob?.cancel()

        _uiState.update { state ->
            state.copy(
                addressQuery = query,
                mapSelection = null,
                street = "",
                house = "",
                isSuggestionsExpanded = query.trim().length >= 3,
                suggestedAddresses = emptyList(),
                error = null
            )
        }

        val normalizedQuery = query.trim()
        if (normalizedQuery.length < 3) {
            _uiState.update {
                it.copy(
                    isSuggestionsExpanded = false,
                )
            }
            return
        }


        val state = _uiState.value

        val city = DeliveryCities.values()
            .firstOrNull { it.name == _uiState.value.city }
            ?: run {
                _uiState.update {
                    it.copy(
                        isSuggestionsExpanded = false,
                    )
                }
                return
            }

        suggestingJob = viewModelScope.launch {
            delay(200)
            try {
                val suggestions = geocoder.geoSuggest(
                    query = normalizedQuery,
                    city = city,
                )

                _uiState.update { state ->
                    state.copy(
                        suggestedAddresses = suggestions .take(6),
                        isSuggestionsExpanded = suggestions.isNotEmpty(),
                        error = null
                    )
                }
            } catch (e: CancellationException){
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        suggestedAddresses = emptyList(),
                        isSuggestionsExpanded = false,
                        error = "Ничего не нашли :("
                    )
                }
            }
        }
    }

    private fun onResolvedAddressSelected(
        selection: AddressMapSelection
    ) {
        _uiState.update { state ->
            state.copy(
                mapSelection = selection,
                city = selection.city ?: state.city,
                street = selection.street.orEmpty(),
                house = selection.house.orEmpty(),
                addressQuery = listOfNotNull(
                    selection.street,
                    selection.house
                )
                    .filter { it.isNotBlank() }
                    .joinToString(", ")
                    .ifBlank { selection.formattedAddress.orEmpty() },
                suggestedAddresses = emptyList(),
                isSuggestionsExpanded = false,
                isResolvingSuggestion = false,
                error = null
            )
        }
    }

    fun clearAddressSuggestions() {
        suggestingJob?.cancel()
        _uiState.update {
            it.copy(
                suggestedAddresses = emptyList(),
                error = null
            )
        }
    }

    fun onSuggestionSelected(item: SuggestItem) {
        when (item.action) {
            SuggestItem.Action.SUBSTITUTE -> {
                // Подставляем текст и автоматически
                // запускаем следующий запрос Geosuggest.
                onAddressQueryChanged(item.displayText ?: item.title.text)
            }

            SuggestItem.Action.SEARCH -> {
                resolveSelectedSuggestion(item)
            }

            SuggestItem.Action.FOLLOW_LINK -> {
                // В форме адреса внешние ссылки не используем.
                clearAddressSuggestions()
            }
        }
    }

    private fun resolveSelectedSuggestion(item: SuggestItem) {
        suggestingJob?.cancel()
        resolvingJob?.cancel()

        val city = DeliveryCities.values()
            .firstOrNull { it.name == _uiState.value.city }
            ?: return

        _uiState.update {
            it.copy(
                suggestedAddresses = emptyList(),
                isSuggestionsExpanded = false,
                isResolvingSuggestion = true,
                error = null
            )
        }

        resolvingJob = viewModelScope.launch {
            try {
                val selection = geocoder.resolveSuggestion(
                    item = item,
                    city = city
                )

                onResolvedAddressSelected(selection)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSuggestionsExpanded = false,
                        isResolvingSuggestion = false,
                        error = "Не удалось определить координаты адреса"
                    )
                }
            }
        }
    }

    fun createAddress() {
        val state = _uiState.value
        if (!validateAddress()) return

        val selection = _uiState.value.mapSelection


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
                        AddressFormEvent.AddressFormCreated(address)
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

    private fun validateAddress(): Boolean {
        val state = _uiState.value

        if (state.isSaving) return false

        val error = when {
            state.city.isBlank() -> {
                "Выберите город"
            }

            DeliveryCities.values().none {
                it.name == state.city
            } -> {
                "В этом городе пока не доставляем :("
            }

            state.addressQuery.isBlank() -> {
                "Укажите улицу и дом"
            }

            state.street.isBlank() -> {
                "Выберите улицу из подсказок"
            }

            state.house.isBlank() -> {
                "Выберите конкретный дом из подсказок"
            }

            state.mapSelection == null -> {
                "Выберите полный адрес из подсказок"
            }

            state.floor.isNotBlank() &&
                    state.floor.toShortOrNull() == null -> {
                "Этаж должен быть целым числом"
            }

            else -> null
        }

        _uiState.update {
            it.copy(error = error)
        }

        return error == null
    }

    fun onTitleChanged(value: String) {
        updateForm { copy(title = value) }
    }

    fun onCitySelected(city: DeliveryCity) {
        suggestingJob?.cancel()

        _uiState.update { state ->
            state.copy(
                city = city.name,
                addressQuery = "",
                street = "",
                house = "",
                entrance = "",
                mapSelection = null,
                suggestedAddresses = emptyList(),
                error = null
            )
        }
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