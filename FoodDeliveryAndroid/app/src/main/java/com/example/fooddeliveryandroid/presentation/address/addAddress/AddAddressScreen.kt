package com.example.fooddeliveryandroid.presentation.address.addAddress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.example.fooddeliveryandroid.presentation.address.map.AddressMapPicker

@Composable
fun AddAddressScreen(
    modifier: Modifier = Modifier,
    onEnterManually: (AddressMapSelection?) -> Unit,
    onContinueToForm: (AddressMapSelection) -> Unit,
    viewModel: AddAddressViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isMapMoving by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.fillMaxSize()) {
        AddressMapPicker(
            modifier = modifier.fillMaxSize(),
            onMapCenterChanged = viewModel::onMapCenterChanged,
            onMapMovingChanged = { isMoving ->
                isMapMoving = isMoving
            }
        )

        PickerMarker()

        AnimatedVisibility (
            visible = !isMapMoving,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),

            enter = slideInVertically { fullHeight -> fullHeight } + fadeIn(),
            exit = slideOutVertically { fullHeight -> fullHeight } + fadeOut()
        ) {
            AddressMapBottomPanel(
                uiState = uiState,
                onEnterManually = {
                    onEnterManually(
                        uiState.selection
                    )
                },
                onContinueToForm = {
                    onContinueToForm(
                        uiState.selection
                    )
                },
            )
        }
    }
}

@Composable
fun BoxScope.PickerMarker() {
    Icon(
        imageVector = Icons.Filled.LocationOn,
        contentDescription = "Выбранное местоположение",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (-20).dp)
            .size(40.dp)
    )
}

@Composable
fun AddressMapBottomPanel(
    uiState: AddAddressUIState,
    onEnterManually: () -> Unit,
    onContinueToForm: () -> Unit,
    modifier: Modifier = Modifier
) {

    val selectedAddress =
        with(uiState.selection){
            AddressMapSelection(
                latitude = latitude,
                longitude = longitude,
                formattedAddress = formattedAddress,
                city = city,
                street = street,
                house = house
            )
    }

    Surface (
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.isGeocoding -> {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                }
                uiState.selection.formattedAddress != null -> {
                    TextButton(
                        onClick = {
                            onEnterManually ()
                        }
                    ) {
                        Text(
                            text = uiState.selection.formattedAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )
                    }

                    AddAddressButton(
                        onContinueToForm = onContinueToForm
                    )

                    EnterAddressManuallyButton (onEnterManually = onEnterManually)
                }

                else -> {
                    Text(
                        uiState.error ?: "Переместите карту, чтобы выбрать адрес",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp
                    )
                    EnterAddressManuallyButton (onEnterManually = onEnterManually)
                }
            }

        }
    }
}

@Composable
fun AddAddressButton(
    onContinueToForm: () -> Unit
) {
    Button(
        onClick = onContinueToForm,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {
        Text(
            text = "Доставить сюда",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EnterAddressManuallyButton(
    onEnterManually: () -> Unit,
) {
    Button(
        onClick = {
            onEnterManually()
        },
        modifier = Modifier
            .fillMaxWidth(0.8f),
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Text("Ввести адрес вручную")
    }
}