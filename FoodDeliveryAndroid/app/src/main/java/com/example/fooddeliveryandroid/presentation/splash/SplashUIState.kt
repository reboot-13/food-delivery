package com.example.fooddeliveryandroid.presentation.splash

sealed interface SplashUIState {
    data object Loading: SplashUIState

    data object GoToCatalog : SplashUIState
}