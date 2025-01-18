package ru.alexp0111.flexypixel.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.alexp0111.flexypixel.R

val flexyPixelTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_mono)),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        color = black,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_mono)),
        fontSize = 24.sp,
        color = black,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_mono)),
        fontSize = 16.sp,
        color = black,
    ),
)