package com.example.romanermilov.food_delivery_backend.exception

class ProductNotFoundException(val id: Long) : RuntimeException("Product with id = $id not found") {
}