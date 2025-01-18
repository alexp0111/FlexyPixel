package ru.alexp0111.flexypixel.ui.core

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import ru.alexp0111.flexypixel.ui.theme.flexyPixelTypography

@Composable
fun LargeTextField(
    text: String,
    weight: FontWeight = FontWeight.Bold,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displayLarge,
        fontWeight = weight,
    )

@Composable
fun MediumTextField(
    text: String,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displayMedium,
        fontWeight = weight,
    )

@Composable
fun SmallTextField(
    text: String,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displaySmall,
        fontWeight = weight,
    )