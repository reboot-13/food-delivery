package com.example.romanermilov.food_delivery_backend.exception

class CartItemNotFoundException(productId: Long): RuntimeException("Could not find product with id: $productId") {
}