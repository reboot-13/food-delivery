package com.example.romanermilov.food_delivery_backend.exception

class PhoneNumberAlreadyExistsException (phoneNumber: String): RuntimeException("user with phone number: $phoneNumber already exists")