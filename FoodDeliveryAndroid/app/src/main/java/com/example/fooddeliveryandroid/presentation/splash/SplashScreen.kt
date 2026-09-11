package com.example.fooddeliveryandroid.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onGoToMainScaffold: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LoadingProcess()

    LaunchedEffect(uiState.value) {
        if (uiState.value is SplashUIState.GoToCatalog) {
            onGoToMainScaffold()
        }
    }

}