package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SegmentUiState
import javax.inject.Inject

class UpperAbstractionLevelConverter @Inject constructor() : IUpperAbstractionLevelConverter {

    override fun convertSegmentListToMatrix(bitmapList: List<Bitmap?>): MutableList<List<SegmentUiState>> =
        mutableListOf<List<SegmentUiState>>().apply {
            bitmapList.windowed(
                CommonSizeConstants.SEGMENTS_MATRIX_SIDE,
                CommonSizeConstants.SEGMENTS_MATRIX_SIDE
            ) { segmentRow ->
                val segmentUiRow = segmentRow.map { bitmap -> SegmentUiState(bitmap) }
                add(segmentUiRow)
            }
        }
}