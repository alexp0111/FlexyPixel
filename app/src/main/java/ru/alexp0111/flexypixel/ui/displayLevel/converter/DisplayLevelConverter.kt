package ru.alexp0111.flexypixel.ui.displayLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import javax.inject.Inject

internal class DisplayLevelConverter @Inject constructor(

) : IDisplayLevelConverter {
    override fun convertOrderedPanelsWithBitmapsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToBitmap: Map<Int, Bitmap?>,
    ): Set<PanelUiModel> =
        panelMetaDataSet.map { panelMetaData ->
            copyGlobalToLocal(PanelUiModel(), panelMetaData).copy(
                bitmap = panelOrderToBitmap[panelMetaData.order]
            )
        }.toSet()

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

    override fun convertGlobalToLocalOnDelete(
        localPanels: Set<PanelUiModel>,
        globalPanels: Set<PanelMetaData>,
        segmentNumber: Int,
    ): Set<PanelUiModel> =
        localPanels.mapNotNull { localPanel ->
            val localAsGlobalPanel = convertPanelUiModelToMetaData(localPanel, segmentNumber)
            val globalPanel = globalPanels.find {
                localAsGlobalPanel.absoluteX == it.absoluteX && localAsGlobalPanel.absoluteY == it.absoluteY
            }
            return@mapNotNull globalPanel?.let { copyGlobalToLocal(localPanel, it) }
        }.toSet()

    override fun convertPanelUiModelToMetaData(panelUiModel: PanelUiModel, segmentNumber: Int): PanelMetaData {
        return PanelMetaData(
            order = panelUiModel.order,
            absoluteX = MatrixConverter.localXtoGlobal(panelUiModel.sourceX, segmentNumber),
            absoluteY = MatrixConverter.localYtoGlobal(panelUiModel.sourceY, segmentNumber),
            status = panelUiModel.status,
            palette = panelUiModel.palette.toMutableList(),
        )
    }

    private fun copyGlobalToLocal(localPanel: PanelUiModel, globalPanel: PanelMetaData): PanelUiModel =
        PanelUiModel(
            bitmap = localPanel.bitmap,
            sourceX = globalPanel.localX,
            sourceY = globalPanel.localY,
            order = globalPanel.order,
            status = globalPanel.status,
            palette = globalPanel.palette,
        )
}