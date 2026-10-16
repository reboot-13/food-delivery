package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.PickupPointApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.PickupPointMapper
import com.example.fooddeliveryandroid.domain.model.PickupPoint
import javax.inject.Inject

class PickupPointRepository @Inject constructor(
    private val pickupPointApi: PickupPointApi
) {

    suspend fun getPickupPoints(): NetworkResult<List<PickupPoint>> {
        return safeApiCall {
            pickupPointApi
                .getPickupPoints()
                .map(PickupPointMapper::responseToModel)
        }
    }

    suspend fun getPickupPoint(
        id: Long
    ): NetworkResult<PickupPoint> {
        return safeApiCall {
            val response = pickupPointApi.getPickupPoint(id)

            PickupPointMapper.responseToModel(response)
        }
    }
}