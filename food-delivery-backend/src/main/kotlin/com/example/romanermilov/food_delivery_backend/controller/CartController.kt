package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.request.AddCartItemRequest
import com.example.romanermilov.food_delivery_backend.dto.request.UpdateCartItemQuantityRequest
import com.example.romanermilov.food_delivery_backend.dto.response.CartItemResponse
import com.example.romanermilov.food_delivery_backend.service.CartItemService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController (
    private val cartItemService: CartItemService
) {
    @GetMapping
    fun getCartItems() : List<CartItemResponse> {
        return cartItemService.getCartItems()
    }

    @PostMapping("/items")
    fun addCartItem(@RequestBody addItemRequest: AddCartItemRequest) : CartItemResponse {
        return cartItemService.addItem(addItemRequest)
    }

    @PatchMapping("/items/{productId}")
    fun updateQuantity(
        @PathVariable("productId") productId : Long,
        @RequestBody quantityRequest: UpdateCartItemQuantityRequest
        ): CartItemResponse {
        return cartItemService.updateQuantity(productId, quantityRequest.quantity)
    }

    @DeleteMapping("/items/{productId}")
    fun deleteCartItem(@PathVariable("productId") productId : Long) {
        cartItemService.deleteItem(productId)
    }

}