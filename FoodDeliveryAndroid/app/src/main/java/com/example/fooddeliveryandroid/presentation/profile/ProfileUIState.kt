package com.example.fooddeliveryandroid.presentation.profile

import com.example.fooddeliveryandroid.domain.model.User

sealed class ProfileUIState {
    data object Loading: ProfileUIState()

    data object UnauthorizedRegister: ProfileUIState()

    data object UnauthorizedAuth: ProfileUIState()

    data class Authorized(val user: User): ProfileUIState()

    data class Error (val message: String): ProfileUIState()
}