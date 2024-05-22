package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap

interface DisplayLevelGlobalStateHolder {
    fun getPanelsConfiguration(segmentNumber: Int): MutableList<Int>

    fun getHolderUpperItem(): Int

    fun getPanelsImages(segmentNumber: Int): MutableMap<Int, Bitmap>

    fun setPanelsConfiguration(segmentNumber: Int, orderToXAndY: MutableMap<Int, Pair<Int, Int>>)
}