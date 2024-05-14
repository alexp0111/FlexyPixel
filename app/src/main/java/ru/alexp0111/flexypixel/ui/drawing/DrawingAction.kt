package ru.alexp0111.flexypixel.ui.drawing

import java.text.FieldPosition


sealed interface DrawingAction {
    data class PickPaletteItem(val paletteItemPosition: Int) : DrawingAction
    data class ChangePaletteItemColor(val drawingColor: DrawingColor) : DrawingAction
    data class ChangePixelColor(val pixelPosition: Int) : DrawingAction
}

interface DrawingActionConsumer {
    fun consumeAction(action: DrawingAction)
}