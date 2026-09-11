package com.example.fooddeliveryandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = Color(0xFFC48787),
    onPrimary = Color.White,

    secondary = Color(0xFFD9AEB0),
    onSecondary = Color(0xFF432B2D),

    background = Color(0xFFFCF5F4),
    onBackground = Color(0xFF49383A),

    surface = Color(0xFFFFFBFA),
    onSurface = Color(0xFF49383A),

    surfaceVariant = Color(0xFFF2E2E1),
    onSurfaceVariant = Color(0xFF6E595A),

    surfaceContainerHigh = Color(0xFFF2E2E1),
    surfaceContainerHighest = Color(0xFFFFFBFA)
)

@Composable
fun FoodDeliveryAndroidTheme(
    content: @Composable () -> Unit,
) {


    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}