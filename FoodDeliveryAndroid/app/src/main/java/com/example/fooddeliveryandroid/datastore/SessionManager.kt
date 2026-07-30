package com.example.fooddeliveryandroid.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @param:ApplicationContext
    private val context: Context
){

    suspend fun saveToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?>{
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun getTokenValue(): String?{
        return context.dataStore.data.first()[TOKEN_KEY]
    }

    suspend fun clearToken(){
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun hasToken(): Boolean{
        return getTokenValue() != null
    }

    private companion object{
        val Context.dataStore by preferencesDataStore(
            name = "session"
        )

        val TOKEN_KEY = stringPreferencesKey(name = "jwt_token")

    }
}