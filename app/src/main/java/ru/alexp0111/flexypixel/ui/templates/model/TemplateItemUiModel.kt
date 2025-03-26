package ru.alexp0111.flexypixel.ui.templates.model

import ru.alexp0111.core_ui.common.model.DrawingColor

internal data class TemplateItemUiModel(
    val enabled: Boolean,
    val pixels: List<DrawingColor>,
    val currentPanel: Int,
)