package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.data.model.PanelMetaData

interface DisplayLevelGlobalStateHolder {
    fun getPanelsConfiguration(segmentNumber: Int): Set<PanelMetaData>

    fun getPanelsAmount(): Int

    fun getPanelsImages(segmentNumber: Int): MutableMap<Int, Bitmap?>

    fun setPanelsConfiguration(segmentNumber: Int, orderToXAndY: MutableMap<Int, Pair<Int, Int>>)
}