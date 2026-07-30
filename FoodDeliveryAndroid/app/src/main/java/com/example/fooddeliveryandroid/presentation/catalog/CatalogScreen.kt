package com.example.fooddeliveryandroid.presentation.catalog


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Category
import com.example.fooddeliveryandroid.domain.model.Product

@Composable
fun CatalogScreen (
    catalogViewModel: CatalogViewModel = hiltViewModel()
){
    val uiState = catalogViewModel.uiState.collectAsStateWithLifecycle()
    when(val state = uiState.value) {
        is CatalogUIState.Success -> {
            Column() {
                Categories(state.catalogData.categories)
                Products(state.catalogData.products)
            }

        }
        is CatalogUIState.Error -> Text(state.message)
        CatalogUIState.Loading -> CircularProgressIndicator()
    }
}

@Composable
fun Categories(categories: List<Category>) {
    LazyRow (horizontalArrangement = Arrangement.SpaceEvenly) {
        items(categories) { category ->
            Text(category.name)

        }
    }
}

@Composable
fun Products(products: List<Product>) {
    LazyColumn() {
        items(products) { product ->
            Column() {
                Text(
                    product.name,
                    fontSize = 20.sp
                    )
                Text(
                    text = product.price.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}