package com.example.fooddeliveryandroid.data.remote.network


import kotlinx.coroutines.CancellationException
import retrofit2.HttpException


suspend fun <T> safeApiCall(apiCall: suspend () -> T) : NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: CancellationException){
        throw e
    } catch (e: HttpException){
        NetworkResult.Error(
            exception = e,
            code = e.code()
        )
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }
}