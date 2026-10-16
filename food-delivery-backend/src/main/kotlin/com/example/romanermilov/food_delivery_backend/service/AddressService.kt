package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.request.CreateAddressRequest
import com.example.romanermilov.food_delivery_backend.dto.request.UpdateAddressRequest
import com.example.romanermilov.food_delivery_backend.dto.response.AddressResponse
import com.example.romanermilov.food_delivery_backend.entity.AddressEntity
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import com.example.romanermilov.food_delivery_backend.exception.IncorrectAuthData
import com.example.romanermilov.food_delivery_backend.exception.AddressNotFoundException
import com.example.romanermilov.food_delivery_backend.mapper.AddressMapper
import com.example.romanermilov.food_delivery_backend.repository.AddressRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AddressService(
    private val addressRepository: AddressRepository
) {

    fun getUserAddresses(): List<AddressResponse> {
        val user = getUser()

        return addressRepository
            .findAllByUserId(requireNotNull(user.id))
            .map(AddressMapper::toResponse)
    }

    fun getAddress(addressId: Long): AddressResponse {
        val user = getUser()
        val address = findUserAddress(user, addressId)

        return AddressMapper.toResponse(address)
    }

    fun createAddress(
        request: CreateAddressRequest
    ): AddressResponse {
        val user = getUser()

        val address = AddressEntity(
            user = user,
            title = request.title,
            city = request.city,
            street = request.street,
            house = request.house,
            apartment = request.apartment,
            entrance = request.entrance,
            floor = request.floor,
            intercom = request.intercom,
            comment = request.comment,
            latitude = request.latitude,
            longitude = request.longitude
        )

        val savedAddress = addressRepository.save(address)

        return AddressMapper.toResponse(savedAddress)
    }

    fun updateAddress(
        addressId: Long,
        request: UpdateAddressRequest
    ): AddressResponse {
        val user = getUser()
        val address = findUserAddress(user, addressId)

        val updatedAddress = AddressEntity(
            id = address.id,
            user = address.user,
            title = request.title,
            city = request.city,
            street = request.street,
            house = request.house,
            apartment = request.apartment,
            entrance = request.entrance,
            floor = request.floor,
            intercom = request.intercom,
            comment = request.comment,
            latitude = request.latitude,
            longitude = request.longitude
        )

        val savedAddress = addressRepository.save(updatedAddress)

        return AddressMapper.toResponse(savedAddress)
    }

    fun deleteAddress(addressId: Long) {
        val user = getUser()
        val address = findUserAddress(user, addressId)

        addressRepository.delete(address)
    }

    private fun findUserAddress(
        user: UserEntity,
        addressId: Long
    ): AddressEntity {
        return addressRepository
            .findByIdAndUserId(
                addressId = addressId,
                userId = requireNotNull(user.id)
            )
            .orElseThrow {
                AddressNotFoundException(addressId)
            }
    }

    private fun getUser(): UserEntity {
        val authentication = SecurityContextHolder
            .getContext()
            .authentication

        return authentication?.principal as? UserEntity
            ?: throw IncorrectAuthData()
    }
}