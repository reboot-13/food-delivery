package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.remote.dto.request.CreateAddressRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateAddressRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.remote.network.api.AddressApi
import com.example.fooddeliveryandroid.data.remote.network.safeApiCall
import com.example.fooddeliveryandroid.domain.mapper.AddressMapper
import com.example.fooddeliveryandroid.domain.model.Address
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val addressApi: AddressApi
) {

    suspend fun getAddresses(): NetworkResult<List<Address>> {
        return safeApiCall {
            addressApi
                .getAddresses()
                .map(AddressMapper::responseToModel)
        }
    }

    suspend fun getAddress(addressId: Long): NetworkResult<Address> {
        return safeApiCall {
            val response = addressApi.getAddress(addressId)
            AddressMapper.responseToModel(response)
        }
    }

    suspend fun createAddress(
        request: CreateAddressRequest
    ): NetworkResult<Address> {
        return safeApiCall {
            val response = addressApi.createAddress(request)
            AddressMapper.responseToModel(response)
        }
    }

    suspend fun updateAddress(
        addressId: Long,
        request: UpdateAddressRequest
    ): NetworkResult<Address> {
        return safeApiCall {
            val response = addressApi.updateAddress(
                addressId = addressId,
                request = request
            )
            AddressMapper.responseToModel(response)
        }
    }

    suspend fun deleteAddress(
        addressId: Long
    ): NetworkResult<Unit> {
        return safeApiCall {
            addressApi.deleteAddress(addressId)
        }
    }
}