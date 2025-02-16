package ru.alexp0111.flexypixel.ui.displayLevel

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.GlobalStateHandler
import ru.alexp0111.flexypixel.business.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.data.model.PanelStatus
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelEffect
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import javax.inject.Inject

internal class DisplayLevelViewModel @Inject constructor(
    globalStateHandlerFactory: GlobalStateHandlerFactory,
    private val router: Router,
    private val displayLevelConverter: IDisplayLevelConverter,
) : MVIViewModel<DisplayLevelIntent, DisplayLevelUiState, DisplayLevelEffect>() {

    private val globalStateHandler by lazy {
        GlobalStateHandler.getInstance(globalStateHandlerFactory, null)
    }

    override fun setInitialState(): DisplayLevelUiState = DisplayLevelUiState()

    override fun handleIntent(intent: DisplayLevelIntent) =
        when (intent) {
            is DisplayLevelIntent.InitSegment -> loadConfiguration(intent.segmentNumber)
            is DisplayLevelIntent.DragAndDrop -> handleDragAndDropIntent(intent.dndIntent)
            is DisplayLevelIntent.Click -> handleClickIntent(intent.panel)
            DisplayLevelIntent.DeletePanels -> { /* todo */ }
            DisplayLevelIntent.DownloadFileToPanels -> { /* todo */ }
            DisplayLevelIntent.GoToPanel -> goToSelectedPanel()
        }

    /* Init */

    private fun loadConfiguration(segmentNumber: Int) {
        launch {
            val panelMetaDataSet = async { globalStateHandler.getPanelsConfiguration(segmentNumber) }
            val panelOrderToBitmap = async { globalStateHandler.getPanelsImages(segmentNumber) }
            val matrix = displayLevelConverter.convertOrderedPanelsWithBitmapsToUiMatrix(
                panelMetaDataSet = panelMetaDataSet.await(),
                panelOrderToBitmap = panelOrderToBitmap.await()
            )

            setState { uiState.value.copy(segmentNumber = segmentNumber, panels = matrix) }
        }
    }

    private fun handleDragAndDropIntent(intent: DisplayLevelDragAndDrop) =
        when (intent) {
            is DisplayLevelDragAndDrop.Up -> handlePanelDrag(intent.panel)
            is DisplayLevelDragAndDrop.Down -> handlePanelDrop(intent.dropX, intent.dropY)
            DisplayLevelDragAndDrop.Fail -> handlePanelDropFail()
        }

    /* Drag and drop */

    private fun handleClickIntent(clickedPanel: PanelUiModel) {
        val clickedPanelState = uiState.value.get(clickedPanel.sourceX, clickedPanel.sourceY) ?: return
        // TODO: On PLACED_WRONG click - show error snackbar
        val newPanel = when (clickedPanelState.status) {
            PanelStatus.DEFAULT -> clickedPanelState.copy(status = PanelStatus.SELECTED)
            PanelStatus.SELECTED -> clickedPanelState.copy(status = PanelStatus.DEFAULT)
            PanelStatus.PLACED_WRONG -> clickedPanelState
        }
        val metaDataPanel = displayLevelConverter.convertPanelUiModelToMetaData(newPanel, uiState.value.segmentNumber)
        val newGlobalConfig = globalStateHandler.updateAndGetValidatedConfig(metaDataPanel)
        val newLocalConfig = displayLevelConverter.convertGlobalToLocalOnUpdate(uiState.value.panels, newGlobalConfig)

        setState { uiState.value.copy(panels = newLocalConfig) }
    }

    private fun handlePanelDrag(draggedPanel: PanelUiModel) =
        setState { uiState.value.copy(draggedPanel = draggedPanel) }

    private fun handlePanelDrop(dropX: Int, dropY: Int) {
        val draggedPanel = uiState.value.draggedPanel ?: return

        val newPanel = updateDraggedPanel(draggedPanel, dropX, dropY)
        val metaDataPanel = displayLevelConverter.convertPanelUiModelToMetaData(newPanel, uiState.value.segmentNumber)
        val newGlobalConfig = globalStateHandler.updateAndGetValidatedConfig(metaDataPanel)
        val newLocalConfig = if (draggedPanel.order == -1) {
            displayLevelConverter.convertGlobalToLocalOnAdd(uiState.value.panels, newGlobalConfig)
        } else {
            displayLevelConverter.convertGlobalToLocalOnUpdate(uiState.value.panels, newGlobalConfig)
        }

        setState { uiState.value.copy(draggedPanel = null, panels = newLocalConfig) }
    }

    private fun updateDraggedPanel(draggedPanel: PanelUiModel, dropX: Int, dropY: Int): PanelUiModel {
        val newOrder = if (draggedPanel.order == -1) globalStateHandler.getPanelsAmount() else draggedPanel.order
        return draggedPanel.copy(
            order = newOrder,
            sourceX = dropX,
            sourceY = dropY,
        )
    }

    private fun handlePanelDropFail() = setState { uiState.value.copy(draggedPanel = null) }

    /* Go to panel */

    private fun goToSelectedPanel() {
        val panel = uiState.value.panels.firstOrNull { it.status == PanelStatus.SELECTED } ?: return
        router.navigateTo(Screens.DrawingScreen(panel.order))
    }
}