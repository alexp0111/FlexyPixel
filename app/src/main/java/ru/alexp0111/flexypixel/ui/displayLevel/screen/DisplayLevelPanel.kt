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
import ru.alexp0111.core_ui.theme.beigeStandard

@Composable
internal fun DisplayLevelPanel(
    panelSize: Int,
) {
    val borderPx = with(LocalDensity.current) { 4.dp.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { 24.dp.toPx() }
    val panelSizeDp = with(LocalDensity.current) { panelSize.toDp() }

    Box(Modifier.size(panelSizeDp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color.White,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                style = Stroke(width = borderPx),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
            drawRoundRect(
                color = beigeStandard,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    }
}