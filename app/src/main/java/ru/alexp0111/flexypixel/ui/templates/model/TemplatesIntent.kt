package ru.alexp0111.flexypixel.ui.templates.model

internal sealed interface TemplatesIntent {
    data class TemplateClicked(val template: TemplateItemUiModel): TemplatesIntent
    data class TemplateRotateClicked(val template: TemplateItemUiModel): TemplatesIntent
    data class TemplatePreviousPanelClicked(val template: TemplateItemUiModel): TemplatesIntent
    data class TemplateNextPanelClicked(val template: TemplateItemUiModel): TemplatesIntent
}