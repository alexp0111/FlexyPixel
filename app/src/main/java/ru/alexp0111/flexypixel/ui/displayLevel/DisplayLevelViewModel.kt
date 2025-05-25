package ru.alexp0111.flexypixel.ui.displayLevel

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandler
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandlerFactory
import ru.alexp0111.core.model.main.PanelStatus
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

    @VisibleForTesting
    val globalStateHandler by lazy {
        GlobalStateHandler.getInstance(globalStateHandlerFactory, null)
    }

    private val isGlobalPanelLimitAchieved
        get() = globalStateHandler.getPanelsAmount() == CommonSizeConstants.PANELS_TOTAL_AMOUNT


    override fun setInitialState(): DisplayLevelUiState = DisplayLevelUiState()

    override fun handleIntent(intent: DisplayLevelIntent) =
        when (intent) {
            is DisplayLevelIntent.InitSegment -> loadConfiguration(intent.segmentNumber)
            is DisplayLevelIntent.DragAndDrop -> handleDragAndDropIntent(intent.dndIntent)
            is DisplayLevelIntent.Click -> handleClickIntent(intent.panel)
            DisplayLevelIntent.DeletePanels -> handleDeleteIntent()
            is DisplayLevelIntent.DownloadFileToPanels -> onDownloadMedia(intent.uri)
            DisplayLevelIntent.GoToPanel -> goToSelectedPanel()
        }

    private fun handleDragAndDropIntent(intent: DisplayLevelDragAndDrop) =
        when (intent) {
            is DisplayLevelDragAndDrop.Up -> handlePanelDrag(intent.panel)
            is DisplayLevelDragAndDrop.Down -> handlePanelDrop(intent.dropCoordinate.x, intent.dropCoordinate.y)
            DisplayLevelDragAndDrop.Fail -> handlePanelDropFail()
        }

    /* Init */

    private fun loadConfiguration(segmentNumber: Int) {
        launch {
            val panelMetaDataSet = async { globalStateHandler.getPanelsConfiguration(segmentNumber) }
            val panelOrderToPixels = async { globalStateHandler.getPanelsImages(segmentNumber) }
            val matrix = displayLevelConverter.convertOrderedPanelsWithPixelsToUiMatrix(
                panelMetaDataSet = panelMetaDataSet.await(),
                panelOrderToPixels = panelOrderToPixels.await()
            )
            val selectedAreaIsSquare = globalStateHandler.checkIfSelectedAreaSquare()

            setState {
                uiState.value.copy(
                    segmentNumber = segmentNumber,
                    panels = matrix,
                    isPanelLimitAchieved = isGlobalPanelLimitAchieved,
                    selectedAreaIsSquare = selectedAreaIsSquare,
                )
            }
        }
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
        val selectedAreaIsSquare = globalStateHandler.checkIfSelectedAreaSquare()

        setState { uiState.value.copy(panels = newLocalConfig, selectedAreaIsSquare = selectedAreaIsSquare) }
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
        val selectedAreaIsSquare = globalStateHandler.checkIfSelectedAreaSquare()

        setState {
            uiState.value.copy(
                draggedPanel = null,
                panels = newLocalConfig,
                isPanelLimitAchieved = isGlobalPanelLimitAchieved,
                selectedAreaIsSquare = selectedAreaIsSquare,
            )
        }
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

    /* Deletion */

    private fun handleDeleteIntent() {
        val newGlobalConfig = globalStateHandler.deleteAndGetValidatedConfig()
        val newLocalConfig = displayLevelConverter.convertGlobalToLocalOnDelete(
            uiState.value.panels,
            newGlobalConfig,
            uiState.value.segmentNumber
        )
        val selectedAreaIsSquare = globalStateHandler.checkIfSelectedAreaSquare()

        setState {
            uiState.value.copy(
                panels = newLocalConfig,
                isPanelLimitAchieved = isGlobalPanelLimitAchieved,
                selectedAreaIsSquare = selectedAreaIsSquare,
            )
        }
    }

    /* Handle media request */

    private fun onDownloadMedia(uri: Uri) {
        globalStateHandler.updateWithUri(uri) {
            loadConfiguration(uiState.value.segmentNumber)
        }
    }

    /* Go to panel */

    private fun goToSelectedPanel() {
        val panel = uiState.value.panels.firstOrNull { it.status == PanelStatus.SELECTED } ?: return
        router.navigateTo(Screens.DrawingScreen(panel.order))
    }
}