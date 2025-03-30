package ru.alexp0111.flexypixel.ui.templates.converter

import ru.alexp0111.flexypixel.database.templates.model.PanelTemplate
import ru.alexp0111.flexypixel.ui.templates.model.TemplateItemUiModel
import javax.inject.Inject

internal class TemplatesConverter @Inject constructor() : ITemplatesConverter {
    override fun convertTemplateToUiModelTemplate(template: PanelTemplate): TemplateItemUiModel =
        TemplateItemUiModel(
            id = template.id,
            pixels = template.pixels,
            orientations = template.orientations,
            enabled = false,
            currentPanel = 0,
        )

    override fun convertUiModelTemplateToTemplate(template: TemplateItemUiModel): PanelTemplate =
        PanelTemplate(
            id = template.id,
            pixels = template.pixels,
            orientations = template.orientations,
        )
}