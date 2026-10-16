package com.example.fooddeliveryandroid.presentation.profile.editProfile

sealed interface EditProfileEvent {
    data object Saved : EditProfileEvent
}