package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.request.CreateOrderRequest
import com.example.romanermilov.food_delivery_backend.dto.response.OrderResponse
import com.example.romanermilov.food_delivery_backend.service.OrderService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController (
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestBody
        @Valid
        request: CreateOrderRequest
    ): OrderResponse {
        return orderService.createOrder(request)
    }

    @GetMapping
    fun getOrdersByUser(): List<OrderResponse> {
        return orderService.getOrdersByUser()
    }

    @GetMapping("{orderId}")
    fun getOrderById(@PathVariable orderId: Long): OrderResponse {
        return orderService.getOrderById(orderId)
    }
}