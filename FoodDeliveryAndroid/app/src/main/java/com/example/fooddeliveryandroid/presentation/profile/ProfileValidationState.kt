package com.example.fooddeliveryandroid.presentation.profile

data class ProfileValidationState (
    val phoneNumber: String? = null,
    val name: String? = null,
    val password: String? = null
) {
    val isValid: Boolean
        get() =
            phoneNumber == null &&
            name == null &&
            password == null
}