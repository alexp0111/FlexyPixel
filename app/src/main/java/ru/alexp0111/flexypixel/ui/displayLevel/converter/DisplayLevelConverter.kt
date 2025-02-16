package ru.alexp0111.flexypixel.ui.displayLevel.converter

import android.graphics.Bitmap
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelStatus
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import javax.inject.Inject

internal class DisplayLevelConverter @Inject constructor(

) : IDisplayLevelConverter {
    override fun convertOrderedPanelsWithBitmapsToUiMatrix(
        panelMetaDataSet: Set<PanelMetaData>,
        panelOrderToBitmap: Map<Int, Bitmap>,
    ): List<List<PanelUiModel>> {
        val matrix = MutableList(CommonSizeConstants.PANELS_MATRIX_SIDE) {
            MutableList(CommonSizeConstants.PANELS_MATRIX_SIDE) {
                PanelUiModel()
            }
        }
        panelMetaDataSet.forEach { panelMetaData ->
            matrix[panelMetaData.localY][panelMetaData.localX] = PanelUiModel(
                bitmap = panelOrderToBitmap[panelMetaData.order],
                sourceX = panelMetaData.localX,
                sourceY = panelMetaData.localY,
                order = panelMetaData.order,
                status = PanelStatus.DEFAULT,
            )
        }
        return matrix
    }

}