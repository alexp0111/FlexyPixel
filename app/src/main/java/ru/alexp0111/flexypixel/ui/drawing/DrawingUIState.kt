package ru.alexp0111.flexypixel.ui.drawing

import kotlinx.coroutines.flow.StateFlow
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.PanelMetaData

data class DrawingUIState(
    val pixelPanel: MutableList<DrawingColor> = MutableList(PanelMetaData.TYPE_64) {
        DrawingColor(9, 9, 9)
    },
    val palette: MutableList<DrawingColor> = MutableList(PanelMetaData.PALETTE_SIZE) {
        DrawingColor(0, 0, 0)
    },
    val chosenPaletteItem: Int = 0,
    val panelPosition: Int = 0
) {
    fun getCurrentDrawingColor() = palette[chosenPaletteItem]
}

interface StateHolder {
    val state: StateFlow<DrawingUIState>
}



