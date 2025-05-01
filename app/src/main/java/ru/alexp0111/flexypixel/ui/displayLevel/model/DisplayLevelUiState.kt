package ru.alexp0111.flexypixel.ui.displayLevel.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.PanelMetaData.Companion.PALETTE_SIZE
import ru.alexp0111.core.model.PanelStatus

@Immutable
internal data class DisplayLevelUiState(
    val segmentNumber: Int = 0,
    val draggedPanel: PanelUiModel? = null,
    val panels: Set<PanelUiModel> = emptySet(),
    val isPanelLimitAchieved: Boolean = false,
    val selectedAreaIsSquare: Boolean = false,
) {
    val isSelectionModeOn: Boolean
        get() = panels.any { it.status == PanelStatus.SELECTED }

    val isOnlyOnePanelSelected: Boolean
        get() = panels.count { it.status == PanelStatus.SELECTED } == 1

    fun get(x: Int, y: Int): PanelUiModel? {
        return panels.find { it.sourceX == x && it.sourceY == y }
    }
}

@Immutable
internal data class PanelUiModel(
    val bitmap: Bitmap? = null,
    val sourceX: Int = -1,
    val sourceY: Int = -1,
    val order: Int = -1,
    val status: PanelStatus = PanelStatus.DEFAULT,
    var palette: List<DrawingColor> = List(PALETTE_SIZE) { DrawingColor(0, 0, 0) },
)