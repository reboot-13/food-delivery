package com.example.fooddeliveryandroid.presentation.address.addAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.domain.geocoder.YandexReverseGeocoder
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val reverseGeocoder: YandexReverseGeocoder,
): ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUIState())
    val uiState = _uiState.asStateFlow()

    private var geocodingJob: Job? = null
    private var requestVersion = 0L

    fun onMapCenterChanged(
        latitude: Double,
        longitude: Double,
        zoom: Float
    ) {
        geocodingJob?.cancel()

        _uiState.update { state ->
            state.copy(
                isGeocoding = true,
                error = null
            )
        }

        val currentRequest = ++requestVersion

        geocodingJob = viewModelScope.launch {
            delay(400)

            try {
                val result = reverseGeocoder.reverseGeocode(
                    point = Point(latitude, longitude),
                    zoom = zoom
                )
                if (currentRequest != requestVersion)
                    return@launch

                _uiState.update { state ->
                    state.copy(
                        selection = AddressMapSelection(
                            latitude = latitude,
                            longitude = longitude,
                            formattedAddress = result.formattedAddress,
                            city = result.city,
                            street = result.street,
                            house = result.house
                        ),
                        isGeocoding = false,
                        error = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (currentRequest == requestVersion) {
                    _uiState.update { state ->
                        state.copy(
                            isGeocoding = false,
                            error = "Не удалось определить адрес, нажмите чтобы ввесьи вручную"
                        )
                    }
                }
            }
        }
    }


    override fun onCleared() {
        reverseGeocoder.cancelCurrentRequest()
        geocodingJob = null
        super.onCleared()
    }
}