package com.example.fooddeliveryandroid.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.repository.CartRepository
import com.example.fooddeliveryandroid.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val userSession: UserSession,
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle.get<String>("productId")).toLong()
    val product =
        catalogRepository
            .observeCartItemById(productId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

}