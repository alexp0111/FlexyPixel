package ru.alexp0111.flexypixel.ui.templates.converter

import ru.alexp0111.local_repo.database.templates.model.PanelTemplate
import ru.alexp0111.flexypixel.ui.templates.model.TemplateItemUiModel

internal interface ITemplatesConverter {
    fun convertTemplateToUiModelTemplate(template: PanelTemplate): TemplateItemUiModel
    fun convertUiModelTemplateToTemplate(template: TemplateItemUiModel): PanelTemplate
}