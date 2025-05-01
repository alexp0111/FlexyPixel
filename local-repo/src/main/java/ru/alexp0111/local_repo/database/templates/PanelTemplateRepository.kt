package ru.alexp0111.local_repo.database.templates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexp0111.local_repo.database.templates.model.PanelTemplate
import ru.alexp0111.local_repo.database.templates.prepared.PreparedTemplatesLoader
import javax.inject.Inject

class PanelTemplateRepository @Inject constructor(
    templateDatabase: PanelTemplateDatabase,
) {
    private val defaultLoader by lazy { PreparedTemplatesLoader() }

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
        templateDao.getAllTemplates().ifEmpty {
            fetchDefaults()
            templateDao.getAllTemplates()
        }
    }

    private suspend fun fetchDefaults() {
        defaultLoader.loadDefaultTemplates().forEach {
            insertTemplate(it)
        }
    }
}