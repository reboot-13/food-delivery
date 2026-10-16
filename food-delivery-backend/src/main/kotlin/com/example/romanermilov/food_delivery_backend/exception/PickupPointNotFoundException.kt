package com.example.romanermilov.food_delivery_backend.exception

class PickupPointNotFoundException(
    id: Long
) : RuntimeException("Pickup point with id=$id not found")