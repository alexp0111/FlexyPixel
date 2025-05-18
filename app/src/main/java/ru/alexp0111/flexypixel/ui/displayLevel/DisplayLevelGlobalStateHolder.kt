package ru.alexp0111.flexypixel.ui.displayLevel

import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.PanelMetaData

interface DisplayLevelGlobalStateHolder {
    fun getPanelsConfiguration(segmentNumber: Int): Set<PanelMetaData>

    fun getPanelsAmount(): Int

    fun getPanelsImages(segmentNumber: Int): MutableMap<Int, List<DrawingColor>?>
}