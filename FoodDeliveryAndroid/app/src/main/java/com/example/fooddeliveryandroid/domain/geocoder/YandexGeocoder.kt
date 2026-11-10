package com.example.fooddeliveryandroid.domain.geocoder

import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.example.fooddeliveryandroid.domain.model.DeliveryCity
import com.example.fooddeliveryandroid.domain.model.GeocodedAddress
import com.example.fooddeliveryandroid.exception.AddressGeocodingException
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import com.yandex.mapkit.search.Address as YandexAddress
import com.yandex.runtime.Error as YandexError
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexGeocoder @Inject constructor() {

    private val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private var currentSession: Session? = null
    private var currentSuggestSession: SuggestSession? = null

    suspend fun geoSuggest(
        query: String,
        city: DeliveryCity,
    ): List<SuggestItem> {
        return suspendCancellableCoroutine { continuation ->
            currentSuggestSession?.reset()
            val suggestSession = searchManager.createSuggestSession()
            currentSuggestSession = suggestSession

            val suggestOptions = SuggestOptions().setSuggestTypes(SuggestType.GEO.value)

            val boundingBox = BoundingBox(
                Point(city.southWestLatitude, city.southWestLongitude),
                Point(city.northEastLatitude, city.northEastLongitude)
            )

            val listener = object : SuggestSession.SuggestListener {
                override fun onResponse(response: SuggestResponse) {
                    if (continuation.isActive) {
                        if (response.items.isEmpty()) {
                            continuation.resumeWithException(IllegalStateException("Ничего не нашли :("))
                        } else {
                            continuation.resume(response.items)
                        }
                    }
                }

                override fun onError(p0: Error) {
                    continuation.resumeWithException(IllegalStateException("Произошла ошибка"))
                }
            }

            suggestSession.suggest(
                query,
                boundingBox,
                suggestOptions,
                listener
            )
        }
    }

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

    suspend fun resolveSuggestion(
        item: SuggestItem,
        city: DeliveryCity
    ): AddressMapSelection = suspendCancellableCoroutine { continuation ->

        currentSession?.cancel()
        currentSession = null

        val boundingBox = BoundingBox(
            Point(
                city.southWestLatitude,
                city.southWestLongitude
            ),
            Point(
                city.northEastLatitude,
                city.northEastLongitude
            )
        )

        val geometry = Geometry.fromBoundingBox(boundingBox)

        val searchOptions = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
        }

        val listener = object : Session.SearchListener {

            override fun onSearchResponse(response: Response) {
                val selection = response.collection.children
                    .mapNotNull { it.obj }
                    .firstNotNullOfOrNull { geoObject ->

                        val metadata = geoObject.metadataContainer
                            .getItem(ToponymObjectMetadata::class.java)
                            ?: return@firstNotNullOfOrNull null

                        val address = metadata.address
                        val components = address.components

                        fun componentName(
                            kind: YandexAddress.Component.Kind
                        ): String? {
                            return components.firstOrNull { component ->
                                kind in component.kinds
                            }?.name
                        }

                        val house = componentName(
                            YandexAddress.Component.Kind.HOUSE
                        )

                        if (house.isNullOrBlank()) {
                            return@firstNotNullOfOrNull null
                        }

                        val point = metadata.balloonPoint

                        AddressMapSelection(
                            latitude = point.latitude,
                            longitude = point.longitude,
                            formattedAddress = address.formattedAddress,
                            city = componentName(
                                YandexAddress.Component.Kind.LOCALITY
                            ) ?: city.name,
                            street = componentName(
                                YandexAddress.Component.Kind.STREET
                            ),
                            house = house
                        )
                    }

                if (!continuation.isActive) return

                if (selection != null) {
                    continuation.resume(selection)
                } else {
                    continuation.resumeWithException(
                        AddressGeocodingException(
                            "Не удалось определить конкретный дом. " +
                                    "Выберите адрес с номером дома."
                        )
                    )
                }
            }

            override fun onSearchError(p0: YandexError) {
                if (continuation.isActive) {
                    continuation.resumeWithException(
                        AddressGeocodingException(
                            "Не удалось получить адрес из поиска Яндекса."
                        )
                    )
                }
            }
        }

        val session = item.uri
            ?.takeIf { it.isNotBlank() }
            ?.let { uri ->
                searchManager.resolveURI(
                    uri,
                    SearchOptions(),
                    listener
                )
            }
            ?: searchManager.submit(
                item.searchText,
                geometry,
                searchOptions,
                listener
            )

        currentSession = session

        continuation.invokeOnCancellation {
            session.cancel()

            if (currentSession === session) {
                currentSession = null
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