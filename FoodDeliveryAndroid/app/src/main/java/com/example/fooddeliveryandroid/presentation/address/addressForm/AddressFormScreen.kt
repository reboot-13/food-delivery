package com.example.fooddeliveryandroid.presentation.address.addressForm

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection

@Composable
fun AddressFormScreen(
    selection: AddressMapSelection?,
    viewModel: AddressFormViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initialize(selection)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        Text(uiState.city)
        Text(uiState.house)
        Text(uiState.street)

        Button(
            onClick = viewModel::createAddress
        ) {
            Text("Привезти сюда")
        }
    }


}