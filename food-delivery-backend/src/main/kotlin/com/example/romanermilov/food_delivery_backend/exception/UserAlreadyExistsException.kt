package com.example.romanermilov.food_delivery_backend.exception

class UserAlreadyExistsException(phoneNumber: String) : RuntimeException("User with phone number ${phoneNumber} already exists") {
}