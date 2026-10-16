package com.example.fooddeliveryandroid.data.remote.network.api

import com.example.fooddeliveryandroid.data.remote.dto.response.PickupPointResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PickupPointApi {

    @GET("pickup-points")
    suspend fun getPickupPoints(): List<PickupPointResponse>

    @GET("pickup-points/{id}")
    suspend fun getPickupPoint(
        @Path("id") id: Long
    ): PickupPointResponse
}