package com.example.fooddeliveryandroid.data.remote.network

sealed class NetworkResult<out T> {

    data class Success <out T>(
        val data: T
    ) : NetworkResult<T>()

    data class Error(
        val exception: Throwable,
        val code: Int? = null
    ) : NetworkResult<Nothing>()
}