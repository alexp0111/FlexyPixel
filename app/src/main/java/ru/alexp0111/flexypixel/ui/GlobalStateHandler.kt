package ru.alexp0111.flexypixel.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.alexp0111.flexypixel.bluetooth.MessageHandler
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.drawing.DrawingGlobalStateHolder
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelGlobalStateHolder

@AssistedFactory
interface GlobalStateHandlerFactory {
    fun create(schemeId: Int?): GlobalStateHandler
}

private const val TAG = "GlobalStateHandler"

class GlobalStateHandler @AssistedInject constructor(
    private val messageHandler: MessageHandler,
    @Assisted private val schemeId: Int?,
) : UpperAbstractionLevelGlobalStateHolder,
    DrawingGlobalStateHolder {

    private var frameCycle: FrameCycle

    init {
        Log.d(TAG, "in init block")
        frameCycle = if (schemeId == null) {
            FrameCycle.getNewInstance()
        } else {
            // TODO: get from bd
            FrameCycle.getNewInstance()
        }
    }

    override fun getSegmentsBitmapImages(): List<Bitmap?> {
        Log.d(TAG, "returnSegmentImages")
        return if (schemeId == null) {
            MutableList(PanelConfiguration.MAX_SIZE) { null }
        } else {
            // TODO: goto BD
            MutableList(PanelConfiguration.MAX_SIZE) {
                Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888).also {
                    Canvas(it).drawColor(Color.BLUE)
                }
            }
        }
    }

    override fun getPanelPixelsConfiguration(panelPosition: Int): MutableList<DrawingColor> {
        val panelSequence = frameCycle.frames.first().panels
        return if (panelPosition > panelSequence.lastIndex) {
            MutableList(PanelMetaData.TYPE_64) { DrawingColor(9, 9, 9) }
        } else {
            mutableListOf<DrawingColor>().apply {
                panelSequence[panelPosition].pixels.forEach {
                    add(DrawingColor.getFromString(it))
                }
            }
        }
    }

    override fun getPanelPalette(panelPosition: Int): MutableList<DrawingColor> {
        return if (panelPosition > frameCycle.configuration.listOfMetaData.lastIndex) {
            MutableList(PanelMetaData.PALETTE_SIZE) { DrawingColor(0, 0, 0) }
        } else {
            frameCycle.configuration.listOfMetaData[panelPosition].palette
        }
    }

    override fun updatePixelColorOnPosition(
        panelPosition: Int,
        color: DrawingColor,
        pixelPosition: Int
    ) {
        if (!isPanelPositionCorrect(panelPosition)) return
        frameCycle.frames.forEachIndexed { index, frame ->
            frame.panels[panelPosition].pixels[pixelPosition] = color.asString()
        }
        // TODO: Send info to DB
        // TODO: Send message to MessageHandler
    }

    override fun setPanelPalette(panelPosition: Int, updatedPalette: MutableList<DrawingColor>) {
        if (!isPanelPositionCorrect(panelPosition)) return
        frameCycle.configuration.listOfMetaData[panelPosition].palette = updatedPalette
        // TODO: Send info to DB
    }

    private fun isPanelPositionCorrect(panelPosition: Int): Boolean {
        frameCycle.configuration.listOfMetaData.apply {
            return isNotEmpty() && panelPosition <= lastIndex
        }
    }


    companion object {
        private var instance: GlobalStateHandler? = null
        fun getInstance(factory: GlobalStateHandlerFactory, schemeId: Int?): GlobalStateHandler {
            return instance ?: factory.create(schemeId).also {
                instance = it
            }
        }

        fun reset() {
            instance = null
        }
    }
}