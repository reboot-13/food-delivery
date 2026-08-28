package com.example.fooddeliveryandroid.presentation.catalog

import com.example.fooddeliveryandroid.domain.model.CatalogData


sealed interface CatalogUIState {
    data object Loading : CatalogUIState

    data class Success (
        val catalogData: CatalogData
    ): CatalogUIState


    data class Error(
        val message: String
    ): CatalogUIState
}