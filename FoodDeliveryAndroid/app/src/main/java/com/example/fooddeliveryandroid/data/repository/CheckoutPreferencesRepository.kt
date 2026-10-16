package com.example.fooddeliveryandroid.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutPreferencesRepository @Inject constructor (
    private val dataStore: DataStore<Preferences>
){

    val orderType: Flow<OrderType> = dataStore.data.map { preferences ->
        when (preferences[ORDER_TYPE_KEY]) {
            OrderType.DELIVERY.name -> OrderType.DELIVERY
            else -> OrderType.PICKUP
        }
    }

    val selectedAddressId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[SELECTED_ADDRESS_ID_KEY]
    }

    val selectedPickupPointId: Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[SELECTED_PICKUP_POINT_ID_KEY]
        }

    suspend fun setOrderType(orderType: OrderType) {
        dataStore.edit { preferences ->
            preferences[ORDER_TYPE_KEY] = orderType.name
        }
    }

    suspend fun setSelectedAddressId(id: Long) {
        dataStore.edit { preferences ->
            preferences[SELECTED_ADDRESS_ID_KEY] = id
        }
    }

    suspend fun clearSelectedAddressId() {
        dataStore.edit { preferences ->
            preferences.remove(SELECTED_ADDRESS_ID_KEY)
        }
    }

    suspend fun setSelectedPickupPoint(id: Long) {
        dataStore.edit { preferences ->
            preferences[SELECTED_PICKUP_POINT_ID_KEY] = id
        }
    }

    suspend fun clearSelectedPickupPointId() {
        dataStore.edit { preferences ->
            preferences.remove(SELECTED_PICKUP_POINT_ID_KEY)
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(ORDER_TYPE_KEY)
            preferences.remove(SELECTED_ADDRESS_ID_KEY)
            preferences.remove(SELECTED_PICKUP_POINT_ID_KEY)
        }
    }

    companion object {
        private val ORDER_TYPE_KEY =
            stringPreferencesKey("order_type")

        private val SELECTED_ADDRESS_ID_KEY =
            longPreferencesKey("selected_address_id")

        private val SELECTED_PICKUP_POINT_ID_KEY =
            longPreferencesKey("selected_pickup_point_id")
    }
}