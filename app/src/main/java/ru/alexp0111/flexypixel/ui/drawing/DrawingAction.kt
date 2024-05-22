package ru.alexp0111.flexypixel.ui.drawing

import ru.alexp0111.flexypixel.data.DrawingColor


sealed interface DrawingAction {
    data object RequestDisplayConfiguration : DrawingAction
    data class LoadDisplayConfiguration(val drawingUIState: DrawingUIState) : DrawingAction
    data class PickPaletteItem(val paletteItemPosition: Int) : DrawingAction
    data class RequestChangePaletteItemColor(
        val colorChannel: ColorChannel,
        val colorChannelValue: Int
    ) : DrawingAction

    data class PaletteItemColorUpdatedSuccessfully(val newPalette: MutableList<DrawingColor>) :
        DrawingAction

    data class RequestPixelColorUpdate(val pixelPosition: Int) : DrawingAction
    data class PixelColorUpdatedSuccessfully(val color: DrawingColor, val pixelPosition: Int) :
        DrawingAction

}

interface DrawingActionConsumer {
    fun consumeAction(action: DrawingAction)
}

enum class ColorChannel {
    RED,
    GREEN,
    BLUE
}