package com.example.fooddeliveryandroid.domain.geocoder

import com.example.fooddeliveryandroid.domain.model.GeocodedAddress
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexReverseGeocoder @Inject constructor() {

    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private var currentSession: Session? = null

    suspend fun reverseGeocode(point: Point, zoom: Float): GeocodedAddress {
        return suspendCancellableCoroutine { continuation ->
            currentSession?.cancel()
            val listener = object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val address = parseResponse(response)
                    if (continuation.isActive) {
                        if (address != null) {
                            continuation.resume(address)
                        } else {
                            continuation.resumeWithException(IllegalStateException("Для выбранной точки не найден адрес"))
                        }
                    }
                }

                override fun onSearchError(e: Error) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(IllegalStateException("Не удалось определить адрес"))
                    }
                }
            }

            val session = searchManager.submit(
                point,
                zoom.toInt(),
                SearchOptions().apply {
                    searchTypes = SearchType.GEO.value
                },
                listener
            )

            currentSession = session

            continuation.invokeOnCancellation {
                session.cancel()
                if (currentSession === session) currentSession = null
            }

        }
    }

    fun cancelCurrentRequest() {
        currentSession?.cancel()
        currentSession = null
    }

    private fun parseResponse(response: Response): GeocodedAddress? {
        val geoObject = response.metadata.toponym
            ?: response.collection.children.firstNotNullOfOrNull { it.obj }
            ?: return null

        val address = geoObject.metadataContainer
            .getItem(ToponymObjectMetadata::class.java)
            ?.address

        if (address != null) {
            val components = address.components

            fun componentName(
                kind: Address.Component.Kind
            ): String? {
                return components.firstOrNull { component ->
                    kind in component.kinds
                }?.name
            }

            return GeocodedAddress(
                formattedAddress = address.formattedAddress,
                city = componentName(Address.Component.Kind.LOCALITY),
                street = componentName(Address.Component.Kind.STREET),
                house = componentName(Address.Component.Kind.HOUSE)
            )
        }

        val fallbackAddress =
            listOfNotNull(geoObject.name, geoObject.descriptionText)
                .filter { it.isNotBlank() }
                .distinct()
                .joinToString(", ")
        return GeocodedAddress(
            formattedAddress = fallbackAddress, null, null, null
        )
    }
}