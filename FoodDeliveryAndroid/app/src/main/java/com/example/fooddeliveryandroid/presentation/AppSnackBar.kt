package com.example.fooddeliveryandroid.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppSnackBar(
    snackBarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(32.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.8f),
                shape = shape
            )
            .background(
                Color.White.copy(alpha = 0.7f)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = snackBarData.visuals.message,
                modifier = Modifier.weight(1f)
            )


            snackBarData.visuals.actionLabel?.let { actionLabel ->

                TextButton(
                    onClick = {
                        snackBarData.performAction()
                    }
                ) {
                    Text(actionLabel)
                }
            }

            if (snackBarData.visuals.withDismissAction) {
                TextButton(
                    onClick = {
                        snackBarData.dismiss()
                    }
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}