package com.example.fooddeliveryandroid.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.fooddeliveryandroid.R
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess

@Composable
fun CartScreen (
    viewModel: CartViewModel = hiltViewModel(),
    onGoToAuthScreen: () -> Unit,
    onGoToCatalog: () -> Unit,
    onShowOrder: (Long) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val createdOrderId by viewModel.createdOrderId.collectAsStateWithLifecycle()

    when (val result = uiState.value) {

        is CartUIState.Loading ->
            LoadingProcess()

        is CartUIState.Unauthorized ->  {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Войдите, чтобы сделать заказ")
                    Button(
                        onClick = onGoToAuthScreen
                    ) {
                        Text("Войти")
                    }
                }
            }
        }
        is CartUIState.Success -> {

            LaunchedEffect(result.cartItems.isNotEmpty()) {
                if (result.cartItems.isNotEmpty() && createdOrderId != null) {
                    viewModel.clearCreatedOrder()
                }
            }

            if (result.cartItems.isEmpty() && createdOrderId != null) {
                OrderCreatedContent(
                    orderId = createdOrderId!!,
                    onNavigateToCatalog = {
                        onGoToCatalog()
                    }
                )
            } else if (result.cartItems.isNotEmpty()) {
                CartSuccess(
                    cartItems = result.cartItems,
                    onUpdateQuantity = { id, quantity ->
                        viewModel.updateCartItemQuantity(
                            productId = id,
                            quantity = quantity
                        )
                    },
                    onCreateOrder = {cartItems ->
                        viewModel.createOrder(cartItems)
                    }
                )
            } else {
                EmptyCartScreen(onGoToCatalog = onGoToCatalog)
            }
        }
    }
}

@Composable
fun EmptyCartScreen(
    onGoToCatalog: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Корзина пуста :(",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Button(
                onClick = onGoToCatalog
            ) {
                Text("Перейти в каталог")
            }
        }
    }
}

@Composable
fun CartSuccess(
    cartItems: List<CartItem>,
    onUpdateQuantity: (Long, Int) -> Unit,
    onCreateOrder: (List<CartItem>) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        items(
            items = cartItems,
            key = { it.product.id }
        ) { cartItem ->
            CartItemCard(
                cartItem = cartItem,
                onUpdateQuantity = onUpdateQuantity
            )
        }
        item {
            Button(
                onClick = {
                    onCreateOrder(cartItems)
                }
            ) {
                Text("Оформить заказ")
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Long, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(32.dp))

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f)) {
                ProductImage(
                    imageUrl = cartItem.product.imageUrl,
                    imageDescription = cartItem.product.name,
                    modifier = Modifier
                        .size(80.dp)
                )
                Column() {
                    Text(
                        text = cartItem.product.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${cartItem.product.price.stripTrailingZeros()} ₽",
                        style = MaterialTheme.typography.bodySmall
                    )
                }


            }

            QuantitySelector(
                quantity = cartItem.quantity,
                onUpdateQuantity = { quantity ->
                    onUpdateQuantity(cartItem.product.id, quantity)
                },
                modifier = Modifier
                    .height(36.dp)
                    .width(120.dp)
            )
        }
    }

}

@Composable
fun ProductImage(
    imageUrl: String?,
    imageDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = imageDescription,
        placeholder = painterResource( R.drawable.empty_product_image),
        error = painterResource(R.drawable.empty_product_image),

        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop)
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onUpdateQuantity: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(36.dp)
            .width(100.dp)
            .clip(shape = RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            onClick = {
                onUpdateQuantity(quantity - 1)
            },
            interactionSource =  interactionSource,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_remove_24),
                contentDescription = "Уменьшить количество"
            )
        }
        Text(
            text = quantity.toString(),
            color = MaterialTheme.colorScheme.onPrimary
        )

        IconButton(
            onClick = {
                onUpdateQuantity(quantity + 1)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_add_24),
                contentDescription = "Увеличить количество"
            )
        }
    }
}


@Composable
fun OrderCreatedContent(
    orderId: Long,
    onNavigateToCatalog: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Заказ создан!",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 24.sp
            )
            Button(
                onClick = onNavigateToCatalog,
                modifier = Modifier.fillMaxWidth(0.5f).height(40.dp)
            ) {
                Text(
                    text = "Оформить ещё один",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}