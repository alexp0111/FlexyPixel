package ru.alexp0111.flexypixel.ui.drawing

import android.graphics.Color
import kotlinx.coroutines.flow.StateFlow
import ru.alexp0111.flexypixel.data.model.Panel


data class DrawingUIState(
    val pixelPanel: MutableList<DrawingColor> = MutableList(Panel.TYPE_64) { DrawingColor(9, 9, 9) },
    val palette: MutableList<DrawingColor> = MutableList(PALETTE_SIZE) { DrawingColor(0, 0, 0) },
    val chosenPaletteItem: Int = 0
) {
    fun getCurrentDrawingColor() = palette[chosenPaletteItem]

    companion object {
        const val PALETTE_SIZE = 12
    }
}

data class DrawingColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {

    private val ratio = 28
    fun toColor() = Color.rgb(r * ratio, g * ratio, b * ratio)

}

interface StateHolder {
    val state: StateFlow<DrawingUIState>
}



