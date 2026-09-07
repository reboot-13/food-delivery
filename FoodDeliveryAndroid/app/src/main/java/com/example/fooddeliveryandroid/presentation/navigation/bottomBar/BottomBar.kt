package com.example.fooddeliveryandroid.presentation.navigation.bottomBar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    currentRoute: String?,
    onRouteChange: (String) -> Unit
) {
    val destinations = listOf(
        BottomDestination.Catalog,
        BottomDestination.Cart,
        BottomDestination.Profile
    )

    val shape = RoundedCornerShape(32.dp)
    Box(
        modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 50.dp, start = 54.dp, end = 54.dp)
        .clip(shape)
            .border(
                width = 1.dp,
                shape = shape,
                color = Color.Gray.copy(alpha = 0.8f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White.copy(alpha = 0.7f))
        ) {
            destinations.forEach { destination ->
                BottomBarItem(
                    bottomDestination = destination,
                    selected = currentRoute == destination.route,
                    onClick = {
                        onRouteChange(destination.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}