package com.example.fooddeliveryandroid.presentation.product

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductModalBottomSheet(
    selectedProductId: Long?,
    onDismissRequest: () -> Unit) {
    if (selectedProductId != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = rememberModalBottomSheetState (
                skipPartiallyExpanded = true
            ),
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp),
        ) {
            ProductScreen(selectedProductId!!)
        }
    }
}