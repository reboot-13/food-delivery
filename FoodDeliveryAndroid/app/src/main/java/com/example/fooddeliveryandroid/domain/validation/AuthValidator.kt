package com.example.fooddeliveryandroid.domain.validation

class AuthValidator {
    private companion object {
        val phoneRegex =
            Regex("^\\+?[1-9]\\d{10,14}\$")
        val passwordRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
    }

    fun validatePhone(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult.Error("Это обязательное поле")
        }

        if (!phoneRegex.matches(phone)) {
            return ValidationResult.Error("Неверный формат телефона")
        }
        return ValidationResult.Success
    }

    fun validatePassword(password: String): ValidationResult{


        if (password.isBlank()) {
            return ValidationResult.Error("Это обязательное поле")
        }

        if (password.length < 8) {
            return ValidationResult.Error(
                "Пароль должен содержать минимум 8 символов"
            )
        }

        return ValidationResult.Success
    }

    fun validateName(name: String): ValidationResult{
        return if (name.isBlank()) ValidationResult.Error("Это обязательное поле")
        else ValidationResult.Success
    }
}