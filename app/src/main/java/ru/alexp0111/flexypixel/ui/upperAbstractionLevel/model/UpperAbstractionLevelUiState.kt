package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import ru.alexp0111.core.CommonSizeConstants

@Immutable
internal data class UpperAbstractionLevelUiState(
    val title: String = "",
    val cardSizePx: Int = 0,
    val segmentMatrix: List<List<SegmentUiState>> =
        List(CommonSizeConstants.SEGMENTS_MATRIX_SIDE) {
            List(CommonSizeConstants.SEGMENTS_MATRIX_SIDE) {
                SegmentUiState()
            }
        },
)

@Immutable
internal data class SegmentUiState(
    val bitmap: Bitmap? = null,
)