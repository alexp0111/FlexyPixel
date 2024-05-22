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
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelGlobalStateHolder
import ru.alexp0111.flexypixel.ui.drawing.DrawingGlobalStateHolder
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelGlobalStateHolder

@AssistedFactory
interface GlobalStateHandlerFactory {
    fun create(schemeId: Int?): GlobalStateHandler
}

private const val TAG = "GlobalStateHandler"
const val EMPTY_CELL = -1

class GlobalStateHandler @AssistedInject constructor(
    private val messageHandler: MessageHandler,
    @Assisted private val schemeId: Int?,
) : UpperAbstractionLevelGlobalStateHolder,
    DrawingGlobalStateHolder,
    DisplayLevelGlobalStateHolder {

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

    /** Upper Abstraction Level */

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

    /** Display Level */

    override fun getPanelsConfiguration(segmentNumber: Int): MutableList<Int> {
        return MutableList(9) { EMPTY_CELL }.apply {
            for (position in 0..8) {
                val panelMetaData = getPanelInSegmentIfExists(position, segmentNumber)
                panelMetaData ?: continue
                this[position] = panelMetaData.order
            }
        }
    }

    private fun getPanelInSegmentIfExists(position: Int, segmentNumber: Int): PanelMetaData? {
        val (x, y) = PanelMetaData.getAbsoluteCoordinates(position, segmentNumber)
        return frameCycle.configuration.getPanelMetaDataByCoordinates(x, y)
    }

    override fun getHolderUpperItem(): Int {
        return frameCycle.configuration.listOfMetaData.size
    }

    override fun getPanelsImages(segmentNumber: Int): MutableMap<Int, Bitmap> {
        return mutableMapOf<Int, Bitmap>().apply {
            getPanelsConfiguration(segmentNumber).forEach { panelOrder ->
                if (panelOrder != EMPTY_CELL) {
                    val panelInStringPixels = frameCycle.frames.first().panels[panelOrder].pixels
                    put(
                        panelOrder,
                        BitmapProcessor.convertPixelStringMatrixToBitmap(panelInStringPixels)
                    )
                }
            }
        }
    }

    /*
    * TODO: Test case with reordering panels. Should we allow it?
    * */
    override fun setPanelsConfiguration(
        segmentNumber: Int,
        orderToXAndY: MutableMap<Int, Pair<Int, Int>>,
    ) {
        orderToXAndY.toSortedMap().forEach {
            val order = it.key
            val absoluteX = it.value.first
            val absoluteY = it.value.second
            if (order <= frameCycle.configuration.listOfMetaData.lastIndex) {
                frameCycle.configuration.listOfMetaData[order].apply {
                    this.absoluteX = absoluteX
                    this.absoluteY = absoluteY
                }
            } else {
                if (order == frameCycle.configuration.listOfMetaData.lastIndex + 1) {
                    frameCycle.configuration.listOfMetaData.add(
                        PanelMetaData(
                            order = order,
                            type = PanelMetaData.TYPE_64,
                            absoluteX = absoluteX,
                            absoluteY = absoluteY,
                            rotation = 0,
                            palette = PanelMetaData.getDefaultPalette()
                        )
                    )
                    frameCycle.frames.onEach { frame ->
                        frame.panels.add(Panel())
                    }
                } else {
                    Log.d(TAG, "New index do not continue current config")
                }
            }
        }
        handleRemovedPanels(segmentNumber, orderToXAndY)
        handleRotation()
    }

    private fun handleRemovedPanels(segmentNumber: Int, orderToXAndY: MutableMap<Int, Pair<Int, Int>>) {
        for (panel in frameCycle.configuration.listOfMetaData) {
            if (panel.order !in orderToXAndY.keys && panel.segment == segmentNumber) {
                removePanelsWithOrderAndHigher(panel.order)
                break
            }
        }
    }

    private fun removePanelsWithOrderAndHigher(order: Int) {
        val amountOfElementsToRemoveFormEnd = frameCycle.configuration.listOfMetaData.size - order
        frameCycle.configuration.listOfMetaData =
            frameCycle.configuration.listOfMetaData.dropLast(amountOfElementsToRemoveFormEnd)
                .toMutableList()
        frameCycle.frames.onEach { frame ->
            frame.panels =
                frame.panels.dropLast(amountOfElementsToRemoveFormEnd).toMutableList()
        }
    }

    private fun handleRotation() {
        frameCycle.configuration.listOfMetaData.forEachIndexed { index, panelMetaData ->
            frameCycle.configuration.listOfMetaData[index] =
                frameCycle.configuration.listOfMetaData[index].copy(
                    rotation = getPanelRotation(panelMetaData)
                )
        }
    }

    private fun getPanelRotation(panelMetaData: PanelMetaData): Int {
        return when (panelMetaData.order) {
            0 -> 0
            frameCycle.configuration.listOfMetaData.lastIndex -> 0
            else -> calculatePanelRotationInChain(panelMetaData)
        }
    }

    private fun calculatePanelRotationInChain(panelMetaData: PanelMetaData): Int {
        val thisPanelX = panelMetaData.absoluteX
        val thisPanelY = panelMetaData.absoluteY
        val nextPanelX = frameCycle.configuration.listOfMetaData[panelMetaData.order + 1].absoluteX
        val nextPanelY = frameCycle.configuration.listOfMetaData[panelMetaData.order + 1].absoluteY
        return when {
            thisPanelY == nextPanelY && thisPanelX > nextPanelX -> 0
            thisPanelY == nextPanelY && thisPanelX < nextPanelX -> 2
            thisPanelY < nextPanelY && thisPanelX == nextPanelX -> 3
            else -> 1
        }
    }

    /** Drawing Level */

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
        frameCycle.frames.onEachIndexed { index, frame ->
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