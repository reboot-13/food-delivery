package com.example.romanermilov.food_delivery_backend.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateUserRequest(

    @field:NotBlank(message = "Имя не может быть пустым")
    @field:Size(max = 100, message = "Имя не может быть длиннее 100 символов")
    val name: String,

    @field:NotBlank(message = "Номер телефона не может быть пустым")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{10,14}$",
        message = "Invalid phone number format")
    val phoneNumber: String
)