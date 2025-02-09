package ru.alexp0111.flexypixel.ui.displayLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel

internal interface IDisplayLevelConverter {
    fun convertOrderedPanelsWithBitmapsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToBitmap: Map<Int, Bitmap>,
    ): List<List<PanelUiModel>>
}