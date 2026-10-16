package com.example.fooddeliveryandroid.presentation.profile.editProfile

data class EditProfileState(
    val name: String = "",
    val phoneNumber: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null
)