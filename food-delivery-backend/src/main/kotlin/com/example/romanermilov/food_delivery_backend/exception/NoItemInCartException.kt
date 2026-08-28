package com.example.romanermilov.food_delivery_backend.exception

class NoItemInCartException (productId : Long) : RuntimeException("No item in cart for product $productId") {
}