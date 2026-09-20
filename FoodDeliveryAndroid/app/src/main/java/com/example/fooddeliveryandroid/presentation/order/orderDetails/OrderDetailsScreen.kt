package com.example.fooddeliveryandroid.presentation.order.orderDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess

@Composable
fun OrderDetailsScreen (
    orderId: Long,
    onGoBack: () -> Unit,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {

    val order by viewModel.order.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.setOrderId(orderId)
    }
    order?.let { order ->
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                orderId = order.id,
                onGoBack = {
                    onGoBack()
                }
            )
            OrderDetailsContent(
                order = order
            )
        }
    } ?: LoadingProcess()
}
@Composable
fun TopBar(
    orderId: Long,
    onGoBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),

        ) {
        IconButton(
            onClick = {
                onGoBack()
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary)
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Назад",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "Заказ $orderId",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}