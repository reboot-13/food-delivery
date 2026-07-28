package com.example.fooddeliveryandroid.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.ProductRepository
import com.example.fooddeliveryandroid.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<CatalogUIState>(CatalogUIState.Loading)
    val uiState: StateFlow<CatalogUIState> = _uiState.asStateFlow()

    init {
        loadProducts()
        }

    private fun loadProducts() {
        viewModelScope.launch {
            when(
                val result: NetworkResult<List<Product>> = productRepository.getAllProducts()
            ) {
                is NetworkResult.Success ->
                    _uiState.value = CatalogUIState.Success(products = result.data)
                is NetworkResult.Error ->
                    _uiState.value = CatalogUIState.Error(message = result.exception.message ?: "Неизвестная ошибка")
            }
        }
    }
}