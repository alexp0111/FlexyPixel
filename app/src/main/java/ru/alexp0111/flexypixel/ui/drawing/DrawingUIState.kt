package ru.alexp0111.flexypixel.ui.drawing

import kotlinx.coroutines.flow.StateFlow
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.DrawingColor

data class DrawingUIState(
    val pixelPanel: MutableList<DrawingColor> = MutableList(PanelMetaData.TYPE_64) { DrawingColor(9, 9, 9) },
    val palette: MutableList<DrawingColor> = MutableList(PALETTE_SIZE) { DrawingColor(0, 0, 0) },
    val chosenPaletteItem: Int = 0
) {
    fun getCurrentDrawingColor() = palette[chosenPaletteItem]

    companion object {
        const val PALETTE_SIZE = 12
    }
}

interface StateHolder {
    val state: StateFlow<DrawingUIState>
}



