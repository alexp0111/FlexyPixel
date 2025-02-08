package ru.alexp0111.flexypixel.ui.displayLevel

import com.github.terrakok.cicerone.Router
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelClick
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelEffect
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import javax.inject.Inject

internal class DisplayLevelViewModel @Inject constructor(
    private val router: Router,
) : MVIViewModel<DisplayLevelIntent, DisplayLevelUiState, DisplayLevelEffect>() {

    override fun setInitialState(): DisplayLevelUiState {
        return DisplayLevelUiState()
    }

    override fun handleIntent(intent: DisplayLevelIntent) =
        when (intent) {
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