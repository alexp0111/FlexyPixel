package ru.alexp0111.flexypixel.ui.templates.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TemplatesUiState(
    val templates: List<TemplateItemUiModel>,
)