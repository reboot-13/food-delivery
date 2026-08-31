package com.example.fooddeliveryandroid.presentation.product

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Product

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val product by viewModel.product.collectAsStateWithLifecycle()

    product?.let {
        ProductContent(
            product = it.product,
            quantity = it.quantity
        )
    }
}

@Composable
fun ProductContent(
    product: Product,
    quantity: Int) {
    Text(product.name, modifier = Modifier.padding(top = 30.dp))
}