package com.example.fooddeliveryandroid.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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