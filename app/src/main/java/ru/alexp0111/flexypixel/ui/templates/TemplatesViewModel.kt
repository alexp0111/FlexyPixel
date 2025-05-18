package ru.alexp0111.flexypixel.ui.templates

import kotlinx.coroutines.launch
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.templates.ITemplatesMessageInteractor
import ru.alexp0111.core.model.main.PanelOrientation
import ru.alexp0111.local_repo.database.templates.PanelTemplateRepository
import ru.alexp0111.flexypixel.ui.templates.converter.ITemplatesConverter
import ru.alexp0111.flexypixel.ui.templates.model.TemplateItemUiModel
import ru.alexp0111.flexypixel.ui.templates.model.TemplatesIntent
import ru.alexp0111.flexypixel.ui.templates.model.TemplatesUiState
import javax.inject.Inject

internal class TemplatesViewModel @Inject constructor(
    private val panelTemplateRepository: PanelTemplateRepository,
    private val templateMessageInteractor: ITemplatesMessageInteractor,
    private val templatesConverter: ITemplatesConverter,
) : MVIViewModel<TemplatesIntent, TemplatesUiState, Nothing>() {

    init {
        launch {
            val templates = panelTemplateRepository.getAllTemplates()
            setState { TemplatesUiState(templates.map(templatesConverter::convertTemplateToUiModelTemplate)) }
        }
    }

    override fun setInitialState(): TemplatesUiState = TemplatesUiState(emptyList())

    override fun handleIntent(intent: TemplatesIntent) =
        when (intent) {
            is TemplatesIntent.TemplateClicked -> onTemplateClicked(intent.template)
            is TemplatesIntent.TemplateNextPanelClicked -> onTemplateNextPanelClicked(intent.template)
            is TemplatesIntent.TemplatePreviousPanelClicked -> onTemplatePreviousPanelClicked(intent.template)
            is TemplatesIntent.TemplateRotateClicked -> onTemplateRotateClicked(intent.template)
        }

    private fun onTemplateClicked(template: TemplateItemUiModel) {
        if (!template.enabled) sendTemplateToMC(template)
        setState {
            copy(templates = templates.map { currentTemplate ->
                if (currentTemplate.id == template.id) {
                    currentTemplate.copy(enabled = !currentTemplate.enabled)
                } else {
                    currentTemplate.copy(enabled = false)
                }
            })
        }
    }

    private fun onTemplateNextPanelClicked(template: TemplateItemUiModel) {
        if (template.currentPanel == CommonSizeConstants.PANELS_TOTAL_AMOUNT - 1) return
        val updatedTemplate = template.copy(currentPanel = template.currentPanel + 1)
        updateTemplateState(updatedTemplate)
    }

    private fun onTemplatePreviousPanelClicked(template: TemplateItemUiModel) {
        if (template.currentPanel == 0) return
        val updatedTemplate = template.copy(currentPanel = template.currentPanel - 1)
        updateTemplateState(updatedTemplate)
    }

    private fun onTemplateRotateClicked(template: TemplateItemUiModel) {
        val newOrientation = when (template.orientations[template.currentPanel]) {
            PanelOrientation.LEFT -> PanelOrientation.UP
            PanelOrientation.UP -> PanelOrientation.RIGHT
            PanelOrientation.RIGHT -> PanelOrientation.DOWN
            PanelOrientation.DOWN -> PanelOrientation.LEFT
        }
        val templateOrientations = template.orientations.mapIndexed { index, panelOrientation ->
            newOrientation.takeIf { index == template.currentPanel } ?: panelOrientation
        }

        template.copy(orientations = templateOrientations).also {
            updateTemplateState(it)
            updateTemplateInDatabase(it)
            sendTemplateToMC(it)
        }
    }

    private fun updateTemplateState(template: TemplateItemUiModel) {
        setState {
            copy(templates = templates.map { currentTemplate ->
                template.takeIf { currentTemplate.id == template.id } ?: currentTemplate
            })
        }
    }

    private fun sendTemplateToMC(template: TemplateItemUiModel) {
        val panelTemplate = templatesConverter.convertUiModelTemplateToTemplate(template)
        templateMessageInteractor.sendTemplate(panelTemplate)
    }

    private fun updateTemplateInDatabase(template: TemplateItemUiModel) {
        launch {
            val panelTemplate = templatesConverter.convertUiModelTemplateToTemplate(template)
            panelTemplateRepository.updateTemplate(panelTemplate)
        }
    }
}