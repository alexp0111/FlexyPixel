package ru.alexp0111.flexypixel.ui.displayLevel.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/* TODO: Refactor this template to original panel ui */
@Composable
internal fun DisplayLevelPanel() {
    val borderPx = with(LocalDensity.current) { 8.dp.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { 24.dp.toPx() }

    Box(Modifier.size(80.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color.Black,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                style = Stroke(width = borderPx),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
            drawRoundRect(
                color = Color.Red,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    }
}