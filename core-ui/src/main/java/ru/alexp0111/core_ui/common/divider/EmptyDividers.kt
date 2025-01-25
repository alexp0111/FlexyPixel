package ru.alexp0111.core_ui.common.divider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalEmptyDivider(height: Dp) = Box(Modifier.height(height))

@Composable
fun HorizontalEmptyDivider(width: Dp) = Box(Modifier.width(width))