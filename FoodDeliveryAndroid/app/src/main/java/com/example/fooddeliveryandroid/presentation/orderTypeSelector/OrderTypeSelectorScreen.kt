package com.example.fooddeliveryandroid.presentation.orderTypeSelector

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.enums.OrderType
import com.example.fooddeliveryandroid.presentation.address.AddressesScreen
import com.example.fooddeliveryandroid.presentation.pickupPoint.PickupPointsScreen

@Composable
fun OrderTypeSelectorScreen(
    viewmodel: OrderTypeSelectorViewModel = hiltViewModel()
) {
    val orderType by viewmodel.orderType.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        OrderTypeSelector(
            selectedOrderType = orderType,
            onOrderTypeSelected = { orderType ->
                viewmodel.setOrderType(orderType)
            }
        )

        when (orderType) {
            OrderType.PICKUP -> {
                PickupPointsScreen ()
            }
            OrderType.DELIVERY -> {
                AddressesScreen()
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderTypeSelector(
    selectedOrderType: OrderType,
    onOrderTypeSelected: (OrderType) -> Unit,
    modifier: Modifier = Modifier
) {

    SecondaryTabRow(
        selectedTabIndex = if (selectedOrderType == OrderType.DELIVERY) 0 else 1,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Tab(
            selected = selectedOrderType == OrderType.DELIVERY,
            onClick =
                {
                    onOrderTypeSelected (OrderType.DELIVERY)
                },
            text = {
                Text("Доставка")
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.DirectionsCar,
                    contentDescription = "Доставка"
                )
            }
        )

        Tab(
            selected = selectedOrderType == OrderType.PICKUP,
            onClick =
                {
                    onOrderTypeSelected (OrderType.PICKUP)
                },
            text = {
                Text("Самовывоз")
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ShoppingBag,
                    contentDescription = "Самовывоз"
                )
            }
        )

    }
}
