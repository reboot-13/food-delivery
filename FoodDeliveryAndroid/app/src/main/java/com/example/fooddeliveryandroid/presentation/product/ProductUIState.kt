package com.example.fooddeliveryandroid.presentation.product

import com.example.fooddeliveryandroid.domain.model.CatalogProduct
import kotlinx.coroutines.flow.Flow

sealed class ProductUIState {
    data object Error: ProductUIState()

    data object Loading: ProductUIState()

    data class Success(val product: Flow<CatalogProduct>): ProductUIState()
}