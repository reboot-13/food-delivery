package com.example.fooddeliveryandroid.domain.validation

sealed interface ValidationResult {

    data object Success: ValidationResult

    data class Error(val message: String): ValidationResult
}