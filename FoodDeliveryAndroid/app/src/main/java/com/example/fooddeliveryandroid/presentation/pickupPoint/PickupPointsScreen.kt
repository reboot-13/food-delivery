package com.example.fooddeliveryandroid.presentation.pickupPoint

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.util.CoilUtils.result
import com.example.fooddeliveryandroid.domain.model.PickupPoint
import com.example.fooddeliveryandroid.presentation.address.AddressIcon
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess

@Composable
fun PickupPointsScreen(
    viewmodel: PickupPointsViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val selectedPickupPoint by viewmodel.selectedPickupPoint.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is PickupPointUIState.Loading -> {
            LoadingProcess()
        }
        is PickupPointUIState.Success -> {
            PickupPointsSuccess(
                pickupPoints = state.pickupPoints,
                selectedPickupPoint = selectedPickupPoint,
                onSelect = { pickupPoint ->
                    viewmodel.selectPickupPoint(
                        pickupPoint = pickupPoint
                    )
                }
            )
        }
        is PickupPointUIState.Error -> {
            Text(state.message)
        }
    }
}

@Composable
fun PickupPointsSuccess (
    pickupPoints: List<PickupPoint>,
    selectedPickupPoint: PickupPoint?,
    onSelect: (PickupPoint) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        snackbarHost = {
            SnackbarHost(
                hostState = remember { SnackbarHostState() }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),

            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                pickupPoints,
                key = { it.id }
            ) { pickupPoint ->
                PickupPointItem(
                    pickupPoint = pickupPoint,
                    isSelected = selectedPickupPoint?.id == pickupPoint.id,
                    onSelect = onSelect
                )
            }
        }
    }

}

@Composable
private fun PickupPointItem(
    pickupPoint: PickupPoint,
    isSelected: Boolean,
    onSelect: (PickupPoint) -> Unit
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .clickable(
                onClick = {
                    onSelect(pickupPoint)
                }
            ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
            ,
            verticalAlignment = Alignment.Top
        ) {
            AddressIcon(isSelected = isSelected)
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    text = pickupPoint.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = pickupPoint.address,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = pickupPoint.workingHours ?: "",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }


}