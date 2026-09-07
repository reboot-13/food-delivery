package com.example.fooddeliveryandroid.presentation.catalog

sealed class CatalogEvent {
    data object ShowAuthSnackBar: CatalogEvent()

    data object ShowSyncSnackBar: CatalogEvent()
}