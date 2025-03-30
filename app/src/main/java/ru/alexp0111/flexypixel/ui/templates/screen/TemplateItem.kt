package ru.alexp0111.flexypixel.ui.templates.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.NeoSlotRounded
import ru.alexp0111.core_ui.common.model.DrawingColor
import ru.alexp0111.core_ui.common.pixels.PixelImage
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.flexypixel.data.model.PanelOrientation
import ru.alexp0111.flexypixel.ui.templates.model.TemplateItemUiModel
import ru.alexp0111.flexypixel.ui.templates.model.TemplatesIntent
import ru.alexp0111.core_ui.R as coreDesignR

@Composable
internal fun TemplateItem(
    template: TemplateItemUiModel,
    intentHandler: (TemplatesIntent) -> Unit = {},
) {
    val cardWidth = 160.dp
    NeoCard(
        modifier = Modifier.width(cardWidth),
        enabled = false,
    ) {
        Column {
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                onClick = { intentHandler(TemplatesIntent.TemplateClicked(template)) },
            ) {
                PixelImage(cardWidth, template.pixels)
            }
            AnimatedVisibility(template.enabled) {
                Column {
                    PanelNumberNavigation(
                        currentPanelNumber = template.currentPanel,
                        onPrevious = { intentHandler(TemplatesIntent.TemplatePreviousPanelClicked(template)) },
                        onNext = { intentHandler(TemplatesIntent.TemplateNextPanelClicked(template)) }
                    )
                    PanelRotateButton(
                        cardWidth = cardWidth,
                        onRotate = { intentHandler(TemplatesIntent.TemplateRotateClicked(template)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PanelNumberNavigation(
    currentPanelNumber: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            modifier = Modifier
                .height(28.dp)
                .clickable { onPrevious() },
            painter = painterResource(coreDesignR.drawable.ic_arrow),
            contentDescription = null,
        )
        NeoSlotRounded(
            modifier = Modifier.size(52.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SmallTextField((currentPanelNumber + 1).toString())
            }
        }
        Image(
            modifier = Modifier
                .rotate(180f)
                .height(28.dp)
                .clickable { onNext() },
            painter = painterResource(coreDesignR.drawable.ic_arrow),
            contentDescription = null,
        )
    }
}

@Composable
private fun PanelRotateButton(
    cardWidth: Dp,
    onRotate: () -> Unit,
) {
    Box(modifier = Modifier.width(cardWidth), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .size(56.dp)
                .padding(16.dp)
                .clickable { onRotate() },
            painter = painterResource(coreDesignR.drawable.ic_rotate),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun TemplateItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(beigeStandard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    TemplateItem(
                        TemplateItemUiModel(
                            id = 0,
                            enabled = true,
                            currentPanel = 4,
                            orientations = List(CommonSizeConstants.PANELS_TOTAL_AMOUNT) { PanelOrientation.LEFT },
                            pixels = Array(64) { listOf("007", "070", "700").random() }
                                .map { DrawingColor.getFromString(it) }
                        )
                    )
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    TemplateItem(
                        TemplateItemUiModel(
                            id = 0,
                            enabled = false,
                            currentPanel = 1,
                            orientations = List(CommonSizeConstants.PANELS_TOTAL_AMOUNT) { PanelOrientation.LEFT },
                            pixels = Array(64) { listOf("007", "070", "700").random() }
                                .map { DrawingColor.getFromString(it) }
                        )
                    )
                }
            }
        }
    }
}