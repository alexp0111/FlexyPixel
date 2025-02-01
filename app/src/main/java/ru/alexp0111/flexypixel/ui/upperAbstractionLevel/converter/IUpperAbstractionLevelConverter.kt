package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SegmentUiState

interface IUpperAbstractionLevelConverter {
    fun convertSegmentListToMatrix(bitmapList: List<Bitmap?>): MutableList<List<SegmentUiState>>
}