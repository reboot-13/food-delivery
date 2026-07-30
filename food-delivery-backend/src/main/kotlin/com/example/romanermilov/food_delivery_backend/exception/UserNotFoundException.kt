package com.example.romanermilov.food_delivery_backend.exception

class UserNotFoundException(phone: String): RuntimeException("User with phone number $phone is not found") {
}