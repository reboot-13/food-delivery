package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.request.CreateAddressRequest
import com.example.romanermilov.food_delivery_backend.dto.request.UpdateAddressRequest
import com.example.romanermilov.food_delivery_backend.dto.response.AddressResponse
import com.example.romanermilov.food_delivery_backend.service.AddressService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/addresses")
class AddressController(
    private val addressService: AddressService
) {

    @GetMapping
    fun getUserAddresses(): List<AddressResponse> {
        return addressService.getUserAddresses()
    }

    @GetMapping("/{addressId}")
    fun getAddress(
        @PathVariable addressId: Long
    ): AddressResponse {
        return addressService.getAddress(addressId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(
        @Valid @RequestBody request: CreateAddressRequest
    ): AddressResponse {
        return addressService.createAddress(request)
    }

    @PutMapping("/{addressId}")
    fun updateAddress(
        @PathVariable addressId: Long,
        @Valid @RequestBody request: UpdateAddressRequest
    ): AddressResponse {
        return addressService.updateAddress(addressId, request)
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAddress(
        @PathVariable addressId: Long
    ) {
        addressService.deleteAddress(addressId)
    }
}