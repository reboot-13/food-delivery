package com.example.romanermilov.food_delivery_backend.exception

class ProductUnavailableException(productName: String): RuntimeException("Product $productName is not available") {
}