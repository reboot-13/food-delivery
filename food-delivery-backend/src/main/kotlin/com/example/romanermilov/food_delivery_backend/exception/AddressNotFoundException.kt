package com.example.romanermilov.food_delivery_backend.exception

class AddressNotFoundException(addressId: Long): RuntimeException ("Address $addressId not found: ")