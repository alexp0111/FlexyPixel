package ru.alexp0111.flexypixel.ui.displayLevel.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel

@Composable
internal fun DisplayLevelPanel(
    panelSize: Int,
    panel: PanelUiModel,
) {
    val borderPx = with(LocalDensity.current) { 2.dp.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { 24.dp.toPx() }
    val panelSizeDp = with(LocalDensity.current) { panelSize.toDp() }

    Box(Modifier.size(panelSizeDp)) {
        panel.bitmap?.let {
            Card(
                Modifier.fillMaxSize().padding(2.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    bitmap = it.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (panel.bitmap == null) {
                drawRoundRect(
                    color = black,
                    size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                    topLeft = Offset(borderPx, borderPx),
                    cornerRadius = CornerRadius(cornerRadiusPx)
                )
            }
            drawRoundRect(
                color = Color.White,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                style = Stroke(width = borderPx),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    }
}

@Preview
@Composable
private fun DisplayLevelPanelPreview() {
    AppTheme {
        DisplayLevelPanel(
            panelSize = 500,
            panel = PanelUiModel(
                bitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.cat_8_8
                )
            )
        )
    }
}