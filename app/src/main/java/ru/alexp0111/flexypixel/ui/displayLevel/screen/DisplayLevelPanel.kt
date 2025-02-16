package ru.alexp0111.flexypixel.ui.displayLevel.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.core_ui.theme.red
import ru.alexp0111.core_ui.util.conditional
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.data.model.PanelStatus
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel

@Composable
internal fun DisplayLevelPanel(
    panelSize: Int,
    panel: PanelUiModel,
) {
    val panelSizeDp = with(LocalDensity.current) { panelSize.toDp() }

    val border = 8.dp
    val cornerRadius = 24.dp
    val innerCornerRadius = 18.dp

    val borderPx = with(LocalDensity.current) { border.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { cornerRadius.toPx() }
    val innerCornerRadiusPx = with(LocalDensity.current) { innerCornerRadius.toPx() }

    val isStrokeEnabled = panel.status == PanelStatus.SELECTED || panel.status == PanelStatus.PLACED_WRONG

    Box(Modifier.size(panelSizeDp)) {
        if (isStrokeEnabled) {
            StrokeDecorator(panel, cornerRadiusPx)
        }
        if (panel.bitmap == null) {
            EmptyPanel(isStrokeEnabled, borderPx, cornerRadiusPx, innerCornerRadiusPx)
        } else {
            BitmapPanel(panel.bitmap, isStrokeEnabled, border, innerCornerRadius, cornerRadius)
        }
    }
}

@Composable
private fun StrokeDecorator(panel: PanelUiModel, cornerRadiusPx: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRoundRect(
            color = if (panel.status == PanelStatus.SELECTED) beigeDark else red,
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(cornerRadiusPx)
        )
    }
}

@Composable
private fun EmptyPanel(
    isStrokeEnabled: Boolean,
    borderPx: Float,
    cornerRadiusPx: Float,
    innerCornerRadiusPx: Float,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (isStrokeEnabled) {
            drawRoundRect(
                color = black,
                size = Size(size.width - borderPx * 2, size.height - borderPx * 2),
                topLeft = Offset(borderPx, borderPx),
                cornerRadius = CornerRadius(innerCornerRadiusPx)
            )
        } else {
            drawRoundRect(
                color = black,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    }
}

@Composable
private fun BitmapPanel(
    bitmap: Bitmap,
    isStrokeEnabled: Boolean,
    border: Dp,
    innerCornerRadius: Dp,
    cornerRadius: Dp,
) {
    Card(
        Modifier
            .fillMaxSize()
            .conditional(isStrokeEnabled) { padding(border) },
        shape = RoundedCornerShape(if (isStrokeEnabled) innerCornerRadius else cornerRadius)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            bitmap = bitmap.asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun DisplayLevelPanelPreviewDefault() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(
                    bitmap = BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                        R.drawable.cat_8_8
                    )
                )
            )
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(bitmap = null)
            )
        }
    }
}

@Preview
@Composable
private fun DisplayLevelPanelPreviewSelected() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(
                    status = PanelStatus.SELECTED,
                    bitmap = BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                        R.drawable.cat_8_8
                    )
                )
            )
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(
                    status = PanelStatus.SELECTED,
                    bitmap = null
                )
            )
        }
    }
}

@Preview
@Composable
private fun DisplayLevelPanelPreviewPlacedWrong() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(
                    status = PanelStatus.PLACED_WRONG,
                    bitmap = BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                        R.drawable.cat_8_8
                    )
                )
            )
            DisplayLevelPanel(
                panelSize = 500,
                panel = PanelUiModel(
                    status = PanelStatus.PLACED_WRONG,
                    bitmap = null
                )
            )
        }
    }
}