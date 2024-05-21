package ru.alexp0111.flexypixel.ui.drawing

import ru.alexp0111.flexypixel.data.DrawingColor

interface DrawingGlobalStateHolder {
    fun getPanelPixelsConfiguration(panelPosition: Int): MutableList<DrawingColor>

    fun getPanelPalette(panelPosition: Int): MutableList<DrawingColor>

    fun updatePixelColorOnPosition(panelPosition: Int, color: DrawingColor, pixelPosition: Int)

    fun setPanelPalette(panelPosition: Int, updatedPalette: MutableList<DrawingColor>)
}