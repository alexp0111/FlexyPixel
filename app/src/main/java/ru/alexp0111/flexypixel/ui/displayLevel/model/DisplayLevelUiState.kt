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
)

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
    PLACED_CORRECT,
    PLACED_WRONG
}