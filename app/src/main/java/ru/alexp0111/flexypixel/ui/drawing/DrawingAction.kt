package ru.alexp0111.flexypixel.ui.drawing


sealed interface DrawingAction {
    data class RequestDisplayConfiguration(val displayPosition: Int): DrawingAction
    data class LoadDisplayConfiguration(val drawingUIState: DrawingUIState) : DrawingAction
    data class PickPaletteItem(val paletteItemPosition: Int) : DrawingAction
    data class ChangePaletteItemColor(val colorChannel: ColorChannel, val colorChannelValue:Int) : DrawingAction
    data class PixelColorUpdatedSuccessfully(val pixelPosition: Int) : DrawingAction
    data class RequestPixelColorUpdate(val pixelPosition: Int) : DrawingAction

}

interface DrawingActionConsumer {
    fun consumeAction(action: DrawingAction)
}

enum class ColorChannel{
    RED,
    GREEN,
    BLUE
}