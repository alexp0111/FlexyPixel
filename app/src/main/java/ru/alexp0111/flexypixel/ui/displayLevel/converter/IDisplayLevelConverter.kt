package ru.alexp0111.flexypixel.ui.displayLevel.converter

import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel

internal interface IDisplayLevelConverter {
    fun convertOrderedPanelsWithPixelsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToPixels: Map<Int, List<DrawingColor>?>,
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

    fun convertGlobalToLocalOnDelete(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>,
        segmentNumber: Int,
    ): Set<PanelUiModel>
}