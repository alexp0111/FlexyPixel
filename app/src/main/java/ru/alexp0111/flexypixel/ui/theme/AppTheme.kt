package ru.alexp0111.flexypixel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) =
    MaterialTheme(
        colorScheme = flexyPixelColorScheme,
        typography = flexyPixelTypography,
        content = content
    )