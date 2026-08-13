package com.example.fooddeliveryandroid.data.remote.network

import com.example.fooddeliveryandroid.data.local.datastore.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking {
            sessionManager.getTokenValue()
        }
        if (token == null) return chain.proceed(request)
        else {
            val interceptedRequest = request.newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer $token"
                )
                .build()
            return chain.proceed(interceptedRequest)
        }
    }
}