package com.example.romanermilov.food_delivery_backend.exception

class CartItemAlreadyExistsException(productId: Long): RuntimeException("The product with id: $productId was already exists") {
}