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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.fooddeliveryandroid.R
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess

@Composable
fun CartScreen (
    viewModel: CartViewModel = hiltViewModel(),
    onGoToAuthScreen: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        is CartUIState.Loading ->
            LoadingProcess()

        is CartUIState.Error ->
            Text((uiState.value as CartUIState.Error).message)
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
        is CartUIState.Success ->
            LazyColumn() {
                items(
                    items = (uiState.value as CartUIState.Success).cartItems,
                    key = { it.product.id }
                    ) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onUpdateQuantity = { quantity ->
                            viewModel.updateCartItemQuantity(
                                productId = cartItem.product.id,
                                quantity = quantity
                            )
                        }
                    )
                }
            }
    }

}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                imageUrl = cartItem.product.imageUrl,
                imageDescription = cartItem.product.name,
                modifier = Modifier
                    .size(80.dp)
            )
            Text(cartItem.product.name)
            QuantitySelector(
                quantity = cartItem.quantity,
                onUpdateQuantity = onUpdateQuantity)
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
    onUpdateQuantity: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
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
