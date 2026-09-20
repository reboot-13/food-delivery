package com.example.fooddeliveryandroid.presentation.order.orderDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddeliveryandroid.domain.model.Order
import com.example.fooddeliveryandroid.domain.model.OrderItem
import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import com.example.fooddeliveryandroid.presentation.cart.ProductImage
import com.example.fooddeliveryandroid.presentation.order.OrderStatusBlockActive
import java.math.BigDecimal

@Composable
fun OrderDetailsContent(
    order: Order,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        StatusBlock(
            activeStatus = order.status
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)){
            items(items = order.items, key = {it.id}) { orderItem ->
                OrderItemCard(orderItem)
            }
        }

        TotalPriceBlock(
            totalPrice = order.totalPrice
        )

    }
}

@Composable
fun TotalPriceBlock(totalPrice: BigDecimal) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(32.dp))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Итого",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 12.sp)
            Text(
                text = "${totalPrice.stripTrailingZeros()} ₽",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Text("Чек заказа")
            }
        }
    }
}

@Composable
fun StatusBlock(
    activeStatus: OrderStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (activeStatus != OrderStatus.CANCELLED) {
            OrderStatusBlockActive(activeStatus)
        } else {
            OrderStatusBlockCancelled()
        }
    }
}

@Composable
fun OrderItemCard(orderItem: OrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .height(80.dp)
        ) {
            Text(
                text = orderItem.productName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${orderItem.priceAtPurchase.stripTrailingZeros()} ₽ x ${orderItem.quantity}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 12.dp)
            )
            ProductImage(
                orderItem.productImageUrl,
                imageDescription = orderItem.productName,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun OrderStatusBlockCancelled() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Заказ отменён",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Red)
                .padding(8.dp)

        )
        Text(
            text = "Заказ отменён",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}