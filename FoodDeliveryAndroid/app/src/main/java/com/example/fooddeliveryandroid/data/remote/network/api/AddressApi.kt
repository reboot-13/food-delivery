package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.response.AddressResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateAddressRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateAddressRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {

    @GET("addresses")
    suspend fun getAddresses(): List<AddressResponse>

    @GET("addresses/{id}")
    suspend fun getAddress(
        @Path("id") addressId: Long
    ): AddressResponse

    @POST("addresses")
    suspend fun createAddress(
        @Body request: CreateAddressRequest
    ): AddressResponse

    @PUT("addresses/{id}")
    suspend fun updateAddress(
        @Path("id") addressId: Long,
        @Body request: UpdateAddressRequest
    ): AddressResponse

    @DELETE("addresses/{id}")
    suspend fun deleteAddress(
        @Path("id") addressId: Long
    )
}