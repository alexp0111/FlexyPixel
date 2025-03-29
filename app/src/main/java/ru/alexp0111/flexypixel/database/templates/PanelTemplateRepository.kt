package ru.alexp0111.flexypixel.database.templates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexp0111.flexypixel.database.templates.model.PanelTemplate
import javax.inject.Inject

internal class PanelTemplateRepository @Inject constructor(
    templateDatabase: PanelTemplateDatabase,
) {
    private val templateDao by lazy {
        templateDatabase.panelTemplateDao()
    }

    suspend fun insertTemplate(template: PanelTemplate): Long = withContext(Dispatchers.IO) {
        templateDao.insertTemplate(template)
    }

    suspend fun updateTemplate(template: PanelTemplate) = withContext(Dispatchers.IO) {
        templateDao.updateTemplate(template)
    }

    suspend fun getAllTemplates(): List<PanelTemplate> = withContext(Dispatchers.IO) {
        templateDao.getAllTemplates()
    }
}