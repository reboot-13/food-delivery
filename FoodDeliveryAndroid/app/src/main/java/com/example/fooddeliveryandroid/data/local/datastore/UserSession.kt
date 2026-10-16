package com.example.fooddeliveryandroid.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    val isAuthorized = _currentUser.map { it != null }


    private val lastCreatedOrderIdKey =
        longPreferencesKey("last_created_order_id")

    val lastCreatedOrderId: Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[lastCreatedOrderIdKey]
        }

    suspend fun saveLastCreatedOrderId(orderId: Long) {
        dataStore.edit { preferences ->
            preferences[lastCreatedOrderIdKey] = orderId
        }
    }

    suspend fun clearLastCreatedOrderId() {
        dataStore.edit { preferences ->
            preferences.remove(lastCreatedOrderIdKey)
        }
    }

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun clear (){
        _currentUser.value = null
    }

    fun addAddress(address: Address) {
        _currentUser.update { user ->
            user?.copy(
                addresses = user.addresses + address
            )
        }
    }

    fun updateAddress(address: Address) {
        _currentUser.update { user ->
            user?.copy(
                addresses = user.addresses.map {
                    if (it.id == address.id) address else it
                }
            )
        }
    }

    fun removeAddress(addressId: Long) {
        _currentUser.update { user ->
            user?.copy(
                addresses = user.addresses.filterNot { it.id == addressId }
            )
        }
    }
}