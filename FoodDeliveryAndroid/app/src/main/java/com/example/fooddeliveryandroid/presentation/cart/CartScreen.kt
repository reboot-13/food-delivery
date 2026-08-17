package com.example.fooddeliveryandroid.presentation.cart

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CartScreen (
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        is CartUIState.Loading ->
            CircularProgressIndicator()
        is CartUIState.Success ->
            Text((uiState.value as CartUIState.Success).data.toString())
        is CartUIState.Error ->
            Text((uiState.value as CartUIState.Error).message)
    }

}
