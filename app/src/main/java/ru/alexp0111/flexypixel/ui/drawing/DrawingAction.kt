package ru.alexp0111.flexypixel.ui.drawing


sealed interface DrawingAction {
    data class RequestDisplayConfiguration(val displayPosition: Int): DrawingAction
    data class LoadDisplayConfiguration(val drawingUIState: DrawingUIState) : DrawingAction
    data class PickPaletteItem(val paletteItemPosition: Int) : DrawingAction
    data class ChangePaletteItemColor(val drawingColor: DrawingColor) : DrawingAction
    data class PixelColorUpdatedSuccessfully(val pixelPosition: Int) : DrawingAction
    data class RequestPixelColorUpdate(val pixelPosition: Int) : DrawingAction

}

interface DrawingActionConsumer {
    fun consumeAction(action: DrawingAction)
}