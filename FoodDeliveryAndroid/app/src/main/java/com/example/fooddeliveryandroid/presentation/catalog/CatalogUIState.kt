package com.example.fooddeliveryandroid.presentation.catalog

import com.example.fooddeliveryandroid.domain.model.CatalogData
import com.example.fooddeliveryandroid.domain.model.Category
import com.example.fooddeliveryandroid.domain.model.Product


sealed interface CatalogUIState {
    data object Loading : CatalogUIState

    data class Success(
        val catalogData: CatalogData
    ) : CatalogUIState

    data class Error(
        val message: String
    ): CatalogUIState
}