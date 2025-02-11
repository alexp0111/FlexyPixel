package ru.alexp0111.flexypixel.ui.displayLevel.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import ru.alexp0111.core.CommonSizeConstants

@Immutable
internal data class DisplayLevelUiState(
    val segmentNumber: Int = 0,
    val draggedPanel: PanelUiModel? = null,
    val panelMatrix: List<List<PanelUiModel>> =
        List(CommonSizeConstants.PANELS_MATRIX_SIDE) {
            List(CommonSizeConstants.PANELS_MATRIX_SIDE) {
                PanelUiModel()
            }
        },
) {
    val isSelectionModeOn: Boolean
        get() = panelMatrix.flatten().any { it.status == PanelStatus.SELECTED }

    val isOnlyOnePanelSelected: Boolean
        get() = panelMatrix.flatten().count { it.status == PanelStatus.SELECTED } == 1
}

@Immutable
internal data class PanelUiModel(
    val bitmap: Bitmap? = null,
    val sourceX: Int = -1,
    val sourceY: Int = -1,
    val order: Int = -1,
    val status: PanelStatus = PanelStatus.DEFAULT
)

@Immutable
internal enum class PanelStatus {
    DEFAULT,
    SELECTED,
    PLACED_WRONG
}