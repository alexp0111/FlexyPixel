package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

const val SEGMENT_MATRIX_SIDE = 3

@Immutable
data class UpperAbstractionLevelUiState(
    val title: String = "TEMPLATE #1",
    val segmentMatrix: List<List<SegmentUiState>> =
        List(SEGMENT_MATRIX_SIDE) {
            List(SEGMENT_MATRIX_SIDE) {
                SegmentUiState()
            }
        },
)

@Immutable
data class SegmentUiState(
    val bitmap: Bitmap? = null,
)