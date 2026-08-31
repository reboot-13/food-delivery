package com.example.fooddeliveryandroid.presentation.catalog


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.CatalogProduct
import com.example.fooddeliveryandroid.domain.model.Category
import com.example.fooddeliveryandroid.domain.model.Product
import com.example.fooddeliveryandroid.presentation.cart.ProductImage
import com.example.fooddeliveryandroid.presentation.cart.QuantitySelector
import kotlinx.coroutines.launch

@Composable
fun CatalogScreen (
    catalogViewModel: CatalogViewModel = hiltViewModel(),
    onProductClick: (Long) -> Unit
){
    val uiState = catalogViewModel.uiState.collectAsStateWithLifecycle()

    when(val state = uiState.value) {
        is CatalogUIState.Loading -> CircularProgressIndicator()
        is CatalogUIState.Success -> {

            CatalogSuccess(
                categories = state.catalogData.categories,
                products = state.catalogData.products,
                onProductClick = { productId ->
                    onProductClick(productId)
                },
                onAddToCart = { productId ->
                    catalogViewModel.addProductToCart(productId)
                },
                onUpdateQuantity = { productId, quantity ->
                    catalogViewModel.updateQuantity(productId, quantity)
                }
            )
        }
        is CatalogUIState.Error -> Text(state.message)
        CatalogUIState.Loading -> CircularProgressIndicator()
    }
}

@Composable
fun CatalogSuccess(
    categories: List<Category>,
    products: List<CatalogProduct>,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit
) {
    val listState = rememberLazyListState()
    val productsByCategory = remember(products) {
        products.groupBy { it.product.category?.id}
    }
    val coroutineScope = rememberCoroutineScope ()

    val currentCategoryIndex by remember {
        derivedStateOf {

            listState.layoutInfo
                .visibleItemsInfo
                .firstOrNull {
                    it.index > 0
                }
                ?.index
                ?.minus(1)
        }
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            CategoryRow(
                categories = categories,
                currentCategoryIndex = currentCategoryIndex,
                onCategoryClick = { categoryIndex ->
                    coroutineScope.launch {
                        listState.animateScrollToItem(categoryIndex + 1, )
                    }
                }
            )
        }

        itemsIndexed(
            items = categories,
            key = { _ , category ->
                category.id
            }
        ) { _, category ->
            CategorySection(
                category = category,
                products = productsByCategory[category.id].orEmpty(),
                onProductClick = onProductClick,
                onAddToCart = onAddToCart,
                onUpdateQuantity = onUpdateQuantity
            )
        }
    }
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    currentCategoryIndex: Int?,
    onCategoryClick: (Int) -> Unit) {
    val categoryListState = rememberLazyListState()

    LazyRow(
        state = categoryListState,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = categories,
            key = {_, category ->
                category.id
            }
        ) {index, category ->
            CategoryChip(
                category = category,
                onCategoryClick = {
                    onCategoryClick(index)
                }
            )
        }
    }
    LaunchedEffect(currentCategoryIndex) {

        if (currentCategoryIndex != null) {

            categoryListState.animateScrollToItem(
                currentCategoryIndex
            )
        }
    }
}
@Composable
fun CategorySection(
    category: Category,
    products: List<CatalogProduct>,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = products,
                key = {it.product.id}
            ) {catalogProduct ->
                ProductCard(
                    product = catalogProduct.product,
                    quantity = catalogProduct.quantity,
                    onProductClick = onProductClick,
                    onAddToCart = onAddToCart,
                    onUpdateQuantity = onUpdateQuantity
                )
            }
        }
    }
}

@Composable
fun ProductCard (
    product: Product,
    quantity: Int,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit){
    Card(
        onClick = { onProductClick(product.id) },
        modifier = Modifier
            .width(180.dp)
            .height(150.dp),

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(product.name)
            ProductImage(
                imageUrl = product.imageUrl,
                imageDescription = product.name
            )
            CatalogQuantitySelector(
                productId = product.id,
                quantity = quantity,
                onAddToCart = onAddToCart,
                onUpdateQuantity = { quantity ->
                    onUpdateQuantity (product.id, quantity)
                }
            )
        }
    }
}

@Composable
fun CatalogQuantitySelector (
    productId: Long,
    quantity: Int,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Int) -> Unit){
    if (quantity < 1) {
        Button(
            onClick = { onAddToCart(productId) }
        ) {
            Text("В корзину")
        }
    } else {
        QuantitySelector(
            quantity = quantity,
            onUpdateQuantity = onUpdateQuantity
        )
    }

}

@Composable
fun CategoryChip(
    category: Category,
    onCategoryClick: () -> Unit
) {
    Button(onClick = onCategoryClick) {
        Text(category.name)
    }
}