package ru.alexp0111.flexypixel.ui.displayLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel

internal interface IDisplayLevelConverter {
    fun convertOrderedPanelsWithBitmapsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToBitmap: Map<Int, Bitmap?>,
    ): Set<PanelUiModel>

    fun convertPanelUiModelToMetaData(panelUiModel: PanelUiModel, segmentNumber: Int): PanelMetaData

    fun convertGlobalToLocalOnUpdate(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>,
    ): Set<PanelUiModel>

    fun convertGlobalToLocalOnAdd(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>,
    ): Set<PanelUiModel>
}