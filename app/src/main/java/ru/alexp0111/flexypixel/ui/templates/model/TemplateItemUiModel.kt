package ru.alexp0111.flexypixel.ui.templates.model

import androidx.compose.runtime.Immutable
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.PanelOrientation

@Immutable
internal data class TemplateItemUiModel(
    val id: Int,
    val enabled: Boolean,
    val pixels: List<DrawingColor>,
    val orientations: List<PanelOrientation>,
    val currentPanel: Int,
)