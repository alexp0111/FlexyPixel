package ru.alexp0111.flexypixel.ui.templates.model

import androidx.compose.runtime.Immutable
import ru.alexp0111.core_ui.common.model.DrawingColor
import ru.alexp0111.flexypixel.data.model.PanelOrientation

@Immutable
internal data class TemplateItemUiModel(
    val id: Int,
    val enabled: Boolean,
    val pixels: List<DrawingColor>,
    val orientations: List<PanelOrientation>,
    val currentPanel: Int,
)