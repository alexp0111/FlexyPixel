package ru.alexp0111.core_ui.common.pixels

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.model.DrawingColor
import ru.alexp0111.core_ui.theme.AppTheme

@Composable
fun PixelImage(
    size: Dp,
    pixels: List<DrawingColor>,
) {
    val pixelSize = with(LocalDensity.current) { size.toPx() } / 8f
    Canvas(modifier = Modifier.size(size)) {
        var offsetX = 0f
        var offsetY = 0f

        for (pixelIndex in 0 until 64) {
            drawRect(
                color = Color(pixels[pixelIndex].toColor()),
                topLeft = Offset(offsetX, offsetY),
                size = Size(pixelSize, pixelSize),
            )
            val shouldGoOnNewLine = (pixelIndex + 1) % 8 == 0
            if (shouldGoOnNewLine) {
                offsetX = 0f
                offsetY += pixelSize
            } else {
                offsetX += pixelSize
            }
        }
    }
}

@Preview
@Composable
private fun PixelImagePreview() {
    AppTheme {
        PixelImage(
            size = 200.dp,
            pixels = Array(64) { listOf("007", "070", "700").random() }
                .map { DrawingColor.getFromString(it) }
        )
    }
}