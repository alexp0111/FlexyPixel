package ru.alexp0111.flexypixel.ui

import android.graphics.Bitmap
import android.util.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.flexypixel.bluetooth.MessageHandler
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.database.schemes.SavedSchemeRepository
import ru.alexp0111.flexypixel.database.schemes.data.UserSavedScheme
import ru.alexp0111.flexypixel.media.IBitmapProcessor
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
    private val databaseRepository: SavedSchemeRepository,
    private val messageHandler: MessageHandler,
    private val bitmapProcessor: IBitmapProcessor,
    @Assisted val schemeId: Int?,
) : UpperAbstractionLevelGlobalStateHolder,
    DrawingGlobalStateHolder,
    DisplayLevelGlobalStateHolder {

    private lateinit var frameCycle: FrameCycle
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        Log.d(TAG, "in init block")
        scope.launch {
            frameCycle = if (schemeId == null) {
                FrameCycle.getNewInstance()
            } else {
                val cycle = databaseRepository.getSchemeById(schemeId).frameCycle
                tryToSendConfigurationToMC(cycle)
                cycle
            }
        }
    }

    /** Upper Abstraction Level */

    override fun getSegmentsBitmapImages(size: Int): List<Bitmap?> {
        val bitmapList = MutableList<Bitmap?>(CommonSizeConstants.SEGMENTS_TOTAL_AMOUNT) { null }
        for (segment in 0..bitmapList.lastIndex) {
            val panelMetaDataSet = getPanelsConfiguration(segment)
            if (panelMetaDataSet.isEmpty()) continue
            bitmapList[segment] = bitmapProcessor.generateBitmapForSegment(panelMetaDataSet, size)
        }
        return bitmapList
    }

    /** Display Level */

    override fun getPanelsConfiguration(segmentNumber: Int): Set<PanelMetaData> {
        val panelMetaDataSet = mutableSetOf<PanelMetaData>()
        for (panelOrder in 0 until CommonSizeConstants.PANELS_TOTAL_AMOUNT) {
            val panelMetaData = getPanelInSegmentIfExists(panelOrder, segmentNumber)
            panelMetaData?.let { panelMetaDataSet.add(it) }
        }
        return panelMetaDataSet
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
            getPanelsConfiguration(segmentNumber).map { it.order }.forEach { panelOrder ->
                if (panelOrder != EMPTY_CELL) {
                    val panelInStringPixels = frameCycle.frames.first().panels[panelOrder].pixels
                    put(
                        panelOrder,
                        bitmapProcessor.convertPixelStringMatrixToBitmap(panelInStringPixels)
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

    private fun handleRemovedPanels(
        segmentNumber: Int,
        orderToXAndY: MutableMap<Int, Pair<Int, Int>>,
    ) {
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
        messageHandler.updateConfiguration(frameCycle.configuration.listOfMetaData)
        messageHandler.sendPixel(
            panelOrder = panelPosition,
            pixelOrder = pixelPosition,
            rChannel = color.r,
            gChannel = color.g,
            bChannel = color.b,
        )
    }

    override fun setPanelPalette(panelPosition: Int, updatedPalette: MutableList<DrawingColor>) {
        if (!isPanelPositionCorrect(panelPosition)) return
        frameCycle.configuration.listOfMetaData[panelPosition].palette = updatedPalette
    }

    fun saveStateToDatabase(title: String) {
        scope.launch {
            if (schemeId == null) {
                save(title)
            } else {
                update(schemeId, title)
            }
        }
    }

    private suspend fun save(title: String) {
        databaseRepository.insertNewScheme(
            UserSavedScheme(
                title = title,
                frameCycle = frameCycle,
            )
        )
    }

    private suspend fun update(schemeId: Int, title: String) {
        databaseRepository.updateExistingScheme(
            UserSavedScheme(
                id = schemeId,
                title = title,
                frameCycle = frameCycle,
            )
        )
    }

    private fun isPanelPositionCorrect(panelPosition: Int): Boolean {
        frameCycle.configuration.listOfMetaData.apply {
            return isNotEmpty() && panelPosition <= lastIndex
        }
    }

    private fun tryToSendConfigurationToMC(cycle: FrameCycle) {
        schemeId ?: return
        messageHandler.updateConfiguration(cycle.configuration.listOfMetaData)
        messageHandler.sendFrames(cycle.frames, cycle.interframeDelay)
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