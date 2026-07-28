package com.example.fooddeliveryandroid.presentation.catalog

import com.example.fooddeliveryandroid.domain.model.Product


sealed interface CatalogUIState {
    data object Loading : CatalogUIState

    data class Success(
        val products: List<Product>
    ) : CatalogUIState

    data class Error(
        val message: String
    ): CatalogUIState
}