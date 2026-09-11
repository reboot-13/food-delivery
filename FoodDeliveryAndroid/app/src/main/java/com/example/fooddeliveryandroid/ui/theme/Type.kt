package com.example.fooddeliveryandroid.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fooddeliveryandroid.R



val Manrope = FontFamily(
    Font(
        R.font.manrope_regular,
        FontWeight.Normal
    ),
    Font(
        R.font.manrope_bold,
        FontWeight.Bold
    ),
    Font(
        R.font.manrope_light,
        FontWeight.Light
    )
)

val RobotoMono = FontFamily(
    Font(
        R.font.robotomono_regular,
        FontWeight.Normal
    ),
    Font(
        R.font.robotomono_medium,
        FontWeight.Medium
    ),
    Font(
        R.font.robotomono_bold,
        FontWeight.Bold,
    ),
    Font(
        R.font.robotomono_light,
        FontWeight.Light
    )
)

val AppTypography = Typography(

    // Большие заголовки экранов
    headlineLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold
    ),

    // Заголовки блоков
    headlineMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold
    ),

    // Названия категорий
    titleLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold
    ),
    // названия товаров
    titleMedium = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal
    ),

    bodyMedium = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal
    ),

    // Мелкий второстепенный текст
    bodySmall = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // Кнопки
    labelLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Medium
    )
)