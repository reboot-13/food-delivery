package com.example.fooddeliveryandroid.presentation.address.addressForm

import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.yandex.mapkit.search.SuggestItem

data class AddressFormUiState(
    val mapSelection: AddressMapSelection? = null,

    val title: String = "",
    val city: String = "",

    val addressQuery: String = "",

    val street: String = "",
    val house: String = "",
    val apartment: String = "",
    val entrance: String = "",
    val floor: String = "",
    val intercom: String = "",
    val comment: String = "",

    val suggestedAddresses: List<SuggestItem>? = null,

    val isSuggestionsExpanded: Boolean = false,
    val isResolvingSuggestion: Boolean = false,

    val isSaving: Boolean = false,
    val error: String? = null
)