package ru.alexp0111.flexypixel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) =
    MaterialTheme(
        colorScheme = lightColorScheme(background = beigeStandard, surface = beigeStandard),
        content = content
    )