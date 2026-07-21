package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.request.AuthRequest
import com.example.romanermilov.food_delivery_backend.dto.request.RegisterRequest
import com.example.romanermilov.food_delivery_backend.dto.response.AuthResponse
import com.example.romanermilov.food_delivery_backend.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
){

    @PostMapping("/register")
    fun register(
        @RequestBody
        @Valid
        registerRequest: RegisterRequest
    ): AuthResponse{
        return userService.registerUser(registerRequest)
    }

    @PostMapping("/auth")
    fun auth(
        @RequestBody
        @Valid
        authRequest: AuthRequest
    ): AuthResponse{
        return userService.authUser(authRequest)
    }

}