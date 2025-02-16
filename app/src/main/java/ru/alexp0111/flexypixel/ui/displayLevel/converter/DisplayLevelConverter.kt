package ru.alexp0111.flexypixel.ui.displayLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import javax.inject.Inject

internal class DisplayLevelConverter @Inject constructor(

) : IDisplayLevelConverter {
    override fun convertOrderedPanelsWithBitmapsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToBitmap: Map<Int, Bitmap?>,
    ): Set<PanelUiModel> =
        panelMetaDataSet.map { panelMetaData ->
            PanelUiModel(
                bitmap = panelOrderToBitmap[panelMetaData.order],
                sourceX = panelMetaData.localX,
                sourceY = panelMetaData.localY,
                order = panelMetaData.order,
                status = panelMetaData.status,
            )
        }.toSet()

    override fun convertPanelUiModelToMetaData(panelUiModel: PanelUiModel, segmentNumber: Int): PanelMetaData {
        return PanelMetaData(
            order = panelUiModel.order,
            absoluteX = MatrixConverter.localXtoGlobal(panelUiModel.sourceX, segmentNumber),
            absoluteY = MatrixConverter.localYtoGlobal(panelUiModel.sourceY, segmentNumber),
            status = panelUiModel.status
        )
    }

    override fun convertGlobalToLocalOnUpdate(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>
    ): Set<PanelUiModel> =
        localPanels.map { localPanel ->
            val globalPanel = globalPanels.find { it.order == localPanel.order }
            if (globalPanel == null) return@map localPanel
            copyGlobalToLocal(localPanel, globalPanel)
        }.toSet()

    override fun convertGlobalToLocalOnAdd(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>,
    ): Set<PanelUiModel> {
        val newGlobalPanel = globalPanels
            .maxBy { it.order }
            .takeIf { global -> global.order !in localPanels.map { it.order } }
        if (newGlobalPanel == null) return convertGlobalToLocalOnUpdate(localPanels, globalPanels)
        return convertGlobalToLocalOnUpdate(localPanels, globalPanels) + copyGlobalToLocal(
            PanelUiModel(),
            newGlobalPanel
        )
    }

    private fun copyGlobalToLocal(localPanel: PanelUiModel, globalPanel: PanelMetaData): PanelUiModel =
        PanelUiModel(
            bitmap = localPanel.bitmap,
            sourceX = globalPanel.localX,
            sourceY = globalPanel.localY,
            order = globalPanel.order,
            status = globalPanel.status,
        )
}