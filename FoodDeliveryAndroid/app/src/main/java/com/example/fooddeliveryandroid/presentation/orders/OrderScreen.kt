package com.example.fooddeliveryandroid.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Order
import com.example.fooddeliveryandroid.domain.model.OrderItem
import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus
import com.example.fooddeliveryandroid.presentation.cart.ProductImage
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when(val result = uiState.value) {
        is OrderUIState.Error -> {

        }
        is OrderUIState.Success -> {
            SuccessOrders(result.orders)
        }
        is OrderUIState.Loading -> LoadingProcess()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessOrders(
    orders: List<Order>,
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    var selectedOrder by rememberSaveable {
        mutableStateOf<Order?>(null)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (selectedOrder != null) {

        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp),
            onDismissRequest = {selectedOrder = null},
            sheetState = sheetState
        ) {
            OrderDetailsContent(selectedOrder!!)
        }
    }


    LazyColumn (
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ){
        items(items = orders, key = {it.id}) { order ->
            OrderCard(
                order = order,
                timeFormatter = timeFormatter,
                dateFormatter = dateFormatter,
                onOrderClick = { order ->
                    selectedOrder = order
                }
            )
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    timeFormatter: DateTimeFormatter,
    dateFormatter: DateTimeFormatter,
    onOrderClick: (Order) -> Unit
) {
    Card(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .fillMaxWidth()
        .height(180.dp)
        .clip(RoundedCornerShape(32.dp)),
        onClick = {
            onOrderClick(order)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Заказ #${order.id}",
                        style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = "${order.totalPrice.stripTrailingZeros()} ₽",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = formatOrderDate(
                        date = order.createdAt,
                        timeFormatter = timeFormatter,
                        dateFormatter = dateFormatter),
                    style = MaterialTheme.typography.bodyMedium
                )
                OrderImageBlock(orderItems = order.items)
                OrderStatusBlock(
                    status = order.status,
                    modifier = Modifier.align(alignment = Alignment.End))
            }
        }
    }
}

@Composable
private fun OrderStatusBlock(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth(0.4f)
    ) {
        Text(
            text = status.description,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun OrderImageBlock(orderItems: List<OrderItem>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        orderItems
            .take(3)
            .forEach { orderItem ->
                ProductImage(
                    imageUrl = orderItem.productImageUrl,
                    imageDescription = orderItem.productName,
                    modifier = Modifier.size(40.dp)
            )
        }
        if (orderItems.size > 3) {
            Text(
                text = "+${orderItems.size - 3}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



fun formatOrderDate(
    date: LocalDateTime,
    timeFormatter: DateTimeFormatter,
    dateFormatter: DateTimeFormatter): String {
    val now = LocalDateTime.now()
    val dateString = date.format(dateFormatter)
    val timeString = date.format(timeFormatter)
    return when {
        now.toLocalDate() == date.toLocalDate() -> {
            "Сегодня в $timeString"
        }
        now.toLocalDate() == date.toLocalDate().minusDays(1) -> {
            "Вчера в $timeString"
        }
        else -> {
            "$dateString в $timeString"
        }
    }
}