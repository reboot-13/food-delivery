package com.example.fooddeliveryandroid.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Product
import com.example.fooddeliveryandroid.presentation.cart.ProductImage
import com.example.fooddeliveryandroid.presentation.cart.QuantitySelector
import java.math.BigDecimal

@Composable
fun ProductScreen(
    productId: Long,
    viewModel: ProductViewModel = hiltViewModel()
) {

    LaunchedEffect(productId)  {
        viewModel.loadProduct(productId)
    }
    val product by viewModel.product.collectAsStateWithLifecycle()
    product?.let {
        ProductContent(
            product = it.product,
            quantity = it.quantity,
            onAddToCart = {
                viewModel.addProductToCart(productId = it.product.id)
            },
            onUpdateQuantity = { quantity ->
                viewModel.updateQuantity(
                    productId = it.product.id,
                    quantity = quantity
                )
            }
        )
    }
}

@Composable
fun ProductContent(
    product: Product,
    quantity: Int,
    onAddToCart: () -> Unit,
    onUpdateQuantity: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProductDetailsImage(
            imageUrl = product.imageUrl,
            imageDescription = product.name
        )
        ProductDetails(
            product,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )

        ProductDetailsQuantitySelector(
            quantity = quantity,
            price = product.price,
            onAddToCart = onAddToCart,
            onUpdateQuantity = onUpdateQuantity
        )


    }
}

@Composable
fun ProductDetailsImage(imageUrl: String?, imageDescription: String) {

    ProductImage(
        imageUrl,
        imageDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
    )
}

@Composable
fun ProductDetails (product: Product, modifier: Modifier) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = product.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp)
            Text(
                text = "${product.weight} г",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        }
        if (!product.description.isNullOrEmpty()) {
            Text(
                text = product.description,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProductDetailsQuantitySelector (
    price: BigDecimal,
    quantity: Int,
    onAddToCart: () -> Unit,
    onUpdateQuantity: (Int) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$price ₽" ,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            modifier = Modifier.weight(0.7f)
        )
        Box(modifier = Modifier
            .weight(0.3f)
        ){
            if (quantity < 1) {
                Button(
                    onClick = { onAddToCart() }
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
    }
}