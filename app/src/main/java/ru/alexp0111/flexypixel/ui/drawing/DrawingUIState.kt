package ru.alexp0111.flexypixel.ui.drawing

import android.graphics.Color
import kotlinx.coroutines.flow.StateFlow



data class DrawingUIState(
    val pixelPanel: ArrayList<DrawingColor> = ArrayList(),
    val palette: ArrayList<DrawingColor> = ArrayList(),
    val chosenPaletteItem: Int = 0
)

data class DrawingColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {

    private val ratio = 28
    fun toColor() = Color.rgb(r * ratio, g * ratio, b * ratio)

}

interface StateHolder {
    val state: StateFlow<DrawingUIState>
}



