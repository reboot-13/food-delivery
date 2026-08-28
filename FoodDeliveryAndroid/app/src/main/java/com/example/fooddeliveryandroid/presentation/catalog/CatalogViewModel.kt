package com.example.fooddeliveryandroid.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.CatalogRepository
import com.example.fooddeliveryandroid.domain.model.CatalogData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
) : ViewModel(){
    val uiState: StateFlow<CatalogUIState> =
        catalogRepository
            .observeCatalogData()
            .map<CatalogData, CatalogUIState>{ data ->
                CatalogUIState.Success(data)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = CatalogUIState.Loading
            )

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            val result = catalogRepository.syncCatalog()
            if (result is NetworkResult.Error) {
                TODO("Обработать ошибку синхронизации")
            }
        }
    }
}
