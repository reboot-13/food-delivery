package com.example.romanermilov.food_delivery_backend.dto.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class CreateAddressRequest(
    @field:Size(max = 50)
    val title: String?,

    @field:NotBlank
    @field:Size(max = 100)
    val city: String,

    @field:NotBlank
    @field:Size(max = 150)
    val street: String,

    @field:NotBlank
    @field:Size(max = 20)
    val house: String,

    @field:Size(max = 20)
    val apartment: String?,

    @field:Size(max = 20)
    val entrance: String?,

    val floor: Short?,

    @field:Size(max = 20)
    val intercom: String?,

    @field:Size(max = 1000)
    val comment: String?,

    @field:DecimalMin("-90.0")
    @field:DecimalMax("90.0")
    val latitude: BigDecimal,

    @field:DecimalMin("-180.0")
    @field:DecimalMax("180.0")
    val longitude: BigDecimal
)