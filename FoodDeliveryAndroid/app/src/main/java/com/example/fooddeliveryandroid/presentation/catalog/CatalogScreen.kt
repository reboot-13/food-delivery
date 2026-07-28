package com.example.fooddeliveryandroid.presentation.catalog


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CatalogScreen (
    catalogViewModel: CatalogViewModel = hiltViewModel()
){
    val uiState = catalogViewModel.uiState.collectAsStateWithLifecycle()
    when(val state = uiState.value) {
        is CatalogUIState.Success -> Text(state.products.toString())
        is CatalogUIState.Error -> Text(state.message)
        CatalogUIState.Loading -> CircularProgressIndicator()
    }
}