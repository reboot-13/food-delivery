package com.example.fooddeliveryandroid.data.remote.network

import kotlinx.coroutines.CancellationException

suspend fun <T> safeApiCall(apiCall: suspend () -> T) : NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: CancellationException){
        throw e
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }
}