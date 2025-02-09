package ru.alexp0111.flexypixel.ui.displayLevel

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelClick
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
            is DisplayLevelIntent.Click -> handleClickIntent(intent.clickIntent)
            DisplayLevelIntent.DeletePanels -> { /* todo */ }
            DisplayLevelIntent.DownloadFileToPanels -> { /* todo */ }
            DisplayLevelIntent.GoToPanel -> { /* todo */ }
        }

    private fun handleDragAndDropIntent(intent: DisplayLevelDragAndDrop) =
        when (intent) {
            is DisplayLevelDragAndDrop.Up -> handlePanelDrag(intent.panel)
            is DisplayLevelDragAndDrop.Down -> handlePanelDrop(intent.dropX, intent.dropY)
            DisplayLevelDragAndDrop.Fail -> handlePanelDropFail()
        }

    private fun handleClickIntent(intent: DisplayLevelClick) =
        when (intent) {
            is DisplayLevelClick.Select -> { /* todo */ }
            is DisplayLevelClick.UnSelect -> { /* todo */ }
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

            setState { uiState.value.copy(segmentNumber = segmentNumber, panelMatrix = matrix) }
        }
    }

    /* Drag and drop */

    private fun handlePanelDrag(draggedPanel: PanelUiModel) =
        setState { uiState.value.copy(draggedPanel = draggedPanel) }

    private fun handlePanelDrop(dropX: Int, dropY: Int) {
        val draggedPanel = uiState.value.draggedPanel ?: return

        // TODO: Extract copy logic to delegate
        setState {
            uiState.value.copy(
                draggedPanel = null,
                panelMatrix = uiState.value.panelMatrix.toMutableList().map { it.toMutableList() }.apply {
                    if (draggedPanel.sourceY != -1 && draggedPanel.sourceX != -1) {
                        this[draggedPanel.sourceY][draggedPanel.sourceX] = PanelUiModel()
                    }
                    this[dropY][dropX] = draggedPanel.copy(sourceX = dropX, sourceY = dropY)
                }
            )
        }
    }

    private fun handlePanelDropFail() = setState { uiState.value.copy(draggedPanel = null) }
}