package com.example.fooddeliveryandroid.presentation.cart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.R
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.model.Order
import com.example.fooddeliveryandroid.domain.model.OrderItem
import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import com.example.fooddeliveryandroid.presentation.order.OrderStatusBar
import com.example.fooddeliveryandroid.presentation.product.ProductImage
import com.example.fooddeliveryandroid.presentation.product.ProductModalBottomSheet
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
    val currentOrder by viewModel.currentOrder.collectAsStateWithLifecycle()
    Scaffold() {innerPadding ->
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
                        order = currentOrder,
                        onNavigateToCatalog = {
                            onGoToCatalog()
                        },
                        onShowOrder = {orderId ->
                            onShowOrder(orderId)
                        },
                        onRepeatOrder = { orderItems ->
                            TODO("Реализовать repeatOrder, скорее всего сделать UseCase - для Cart и OrderDetails VM")
                        }
                    )
                } else if (result.cartItems.isNotEmpty()) {
                    ShoppingCart(
                        viewModel = viewModel,
                        cartItems = result.cartItems,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                } else {
                    EmptyCartScreen(onGoToCatalog = onGoToCatalog)
                }
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
fun ShoppingCart(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel,
    cartItems: List<CartItem>
) {
    var selectedProductId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    ProductModalBottomSheet(
        selectedProductId = selectedProductId,
        onDismissRequest = {
            selectedProductId = null
        }
    )

    val haveUnavailableProduct = cartItems.any {
        !it.product.available
    }

    val totalPrice = cartItems.sumOf { item ->
        item.product.price.toInt() * item.quantity
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 14.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Способ получения:",
            style = MaterialTheme.typography.labelLarge,

        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = cartItems,
                key = {it.product.id}
            ) { cartItem ->
                SwipeToDeleteCartItem(
                    cartItem = cartItem,
                    onUpdateQuantity = { id, quantity ->
                        viewModel.updateCartItemQuantity(productId = id, quantity = quantity)
                    },
                    onShowProduct = {
                        selectedProductId = it
                    },
                    modifier = Modifier.animateItem(
                        placementSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        fadeInSpec = tween(150),
                        fadeOutSpec = tween (200)
                    )
                )
            }
            item (key = "unavailableWarning"){
                if (haveUnavailableProduct) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(
                                placementSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                                fadeInSpec = tween(200),
                                fadeOutSpec = tween(150)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.secondary
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "У нас кое-что закончилось :( \nУдалите недоступные товары из корзины, чтобы оформить заказ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }

            item (key = "totalPrice") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            placementSpec = spring(
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            fadeInSpec = tween(200),
                            fadeOutSpec = tween(150)
                        )
                ) {
                    Text(
                        text = "Итого",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "$totalPrice ₽",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            viewModel.createOrder(cartItems)
                        },
                        enabled = !haveUnavailableProduct
                    ) {
                        Text("Оформить заказ")
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteCartItem(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onUpdateQuantity: (Long, Int) -> Unit,
    onShowProduct: (Long) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState ()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                onUpdateQuantity(cartItem.product.id, 0)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier = modifier
            .fillMaxWidth(),
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteSweep,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(30.dp)
                )
            }
        }
    ) {
        CartItemCard(
            cartItem = cartItem,
            onShowProduct = onShowProduct,
            onUpdateQuantity = onUpdateQuantity
        )
    }


}

@Composable
fun CartItemCard (
    cartItem: CartItem,
    onUpdateQuantity: (Long, Int) -> Unit,
    onShowProduct: (Long) -> Unit
) {
    val isAvailable = cartItem.product.available

    val cardColor = if (isAvailable) {
        Color.White
    } else {
        Color(0xFFF0ECEE)
    }


    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cardColor,
        border = if (isAvailable) {
            null
        } else {
            BorderStroke(1.dp, Color(0xFFE7DDE0))
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onShowProduct(cartItem.product.id)
                }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                ProductImage(
                    imageUrl = cartItem.product.imageUrl,
                    imageDescription = cartItem.product.name,
                    modifier = Modifier.fillMaxSize()
                )

                if (!isAvailable) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Color.Black.copy(alpha = 0.42f)
                            )
                    ){
                        Text(
                            text = "Раскупили",
                            modifier = Modifier
                                .align (Alignment.Center),
                            color = Color.LightGray,
                            textAlign = TextAlign.Center)
                    }

                }
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (cartItem.product.weight != null) {
                    Text(
                        text = "${cartItem.product.weight} г",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E7D7F)
                    )
                }
                QuantitySelector(
                    quantity = cartItem.quantity,
                    onUpdateQuantity = { quantity ->
                        onUpdateQuantity(
                            cartItem.product.id,
                            quantity
                        )
                    },
                    modifier = Modifier
                        .height(32.dp)
                        .width(126.dp),
                    bgColor = if (isAvailable) {
                        Color(0xFFF7EEEE)
                    } else {
                        Color(0xFFE5E0E2)
                    },
                    contentColor = if (isAvailable) {
                        Color(0xFF564B4D)
                    } else {
                        Color(0xFF958C90)
                    },
                    canIncrease = isAvailable
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${cartItem.product.price.toInt() * cartItem.quantity} ₽ "
            )
        }

    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onUpdateQuantity: (Int) -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    canIncrease: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = bgColor,
        contentColor = contentColor,
        tonalElevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier


        ) {
            IconButton(
                onClick = {
                    onUpdateQuantity(quantity - 1)
                },
                interactionSource =  interactionSource,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = contentColor
                )
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_remove_24),
                    contentDescription = "Уменьшить количество"
                )
            }
            Text(
                text = quantity.toString(),
                color = contentColor
            )

            IconButton(
                onClick = {
                    onUpdateQuantity(quantity + 1)
                },
                enabled = canIncrease,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = contentColor
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_add_24),
                    contentDescription = "Увеличить количество"
                )
            }
        }
    }
}




@Composable
fun OrderCreatedContent(
    order: Order?,
    onNavigateToCatalog: () -> Unit,
    onShowOrder: (Long) -> Unit,
    onRepeatOrder: (List<OrderItem>) -> Unit){

    if (order != null) {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        val isPressed by interactionSource.collectIsPressedAsState()

        val scale by animateFloatAsState(
            targetValue = if (isPressed) {
                0.97f
            } else {
                1f
            },
            animationSpec = tween(120),
            label = "status_block_scale"
        )

        val buttonText =
            if (order.status == OrderStatus.CANCELLED)
                "Повторить заказ"
            else
                "Оформить ещё один"

        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .padding(horizontal = 16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = order.status.specification,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 24.sp
                )

                Button(
                    onClick = {
                            if (order.status == OrderStatus.CANCELLED)
                                onRepeatOrder(order.items)
                            else
                                onNavigateToCatalog()
                        },
                    modifier = Modifier.fillMaxWidth(0.5f).height(40.dp)
                ) {

                    Text(
                        text = buttonText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Surface(
                onClick = {
                    onShowOrder(order.id)
                },
                interactionSource = interactionSource,
                shadowElevation = 1.dp,
                shape = RoundedCornerShape(40.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .padding(bottom = 12.dp)
            ) {
                OrderStatusBar(
                    activeStatus = order.status,
                    withRightArrow = true
                )
            }
        }
    }
}