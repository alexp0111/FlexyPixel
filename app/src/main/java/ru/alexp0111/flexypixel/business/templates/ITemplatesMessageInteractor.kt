package ru.alexp0111.flexypixel.business.templates

import ru.alexp0111.local_repo.database.templates.model.PanelTemplate

internal interface ITemplatesMessageInteractor {
    fun sendTemplate(template: PanelTemplate)
}