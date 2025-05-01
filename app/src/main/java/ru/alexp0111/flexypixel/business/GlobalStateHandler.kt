package ru.alexp0111.flexypixel.business

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexp0111.bt_service.business.IMessageHandler
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.flexypixel.business.panel_validation.IPanelPositionValidator
import ru.alexp0111.core.model.Frame
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.Panel
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.core.model.PanelStatus
import ru.alexp0111.local_repo.database.schemes.SavedSchemeRepository
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.media_loader.IMediaFilesProcessor
import ru.alexp0111.model.MediaFileType
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelGlobalStateHolder
import ru.alexp0111.flexypixel.ui.drawing.DrawingGlobalStateHolder
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelGlobalStateHolder
import kotlin.math.abs

@AssistedFactory
internal interface GlobalStateHandlerFactory {
    fun create(schemeId: Int?): GlobalStateHandler
}

private const val TAG = "GlobalStateHandler"
const val EMPTY_CELL = -1

internal class GlobalStateHandler @AssistedInject constructor(
    private val databaseRepository: SavedSchemeRepository,
    private val messageHandler: IMessageHandler,
    private val bitmapProcessor: IBitmapProcessor,
    private val mediaFileProcessor: IMediaFilesProcessor,
    private val panelValidator: IPanelPositionValidator,
    @Assisted var schemeId: Int?,
) : UpperAbstractionLevelGlobalStateHolder,
    DrawingGlobalStateHolder,
    IPanelPositionValidator by panelValidator,
    DisplayLevelGlobalStateHolder {

    private lateinit var frameCycle: FrameCycle
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("TAG", throwable.message.toString())
    }
    private val scope = CoroutineScope(Dispatchers.IO + errorHandler)

    private val metaDataSet: MutableSet<PanelMetaData>
        get() = frameCycle.configuration.listOfMetaData

    init {
        Log.d(TAG, "in init block")
        scope.launch {
            val id = schemeId
            frameCycle = if (id == null) {
                messageHandler.clearCurrentState()
                FrameCycle.getNewInstance()
            } else {
                val cycle = databaseRepository.getSchemeById(id).frameCycle
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
        return validatedConfig().filter { it.segment == segmentNumber }.toSet()
    }

    override fun getPanelsAmount(): Int {
        return metaDataSet.size
    }

    override fun getPanelsImages(segmentNumber: Int): MutableMap<Int, Bitmap?> {
        return mutableMapOf<Int, Bitmap?>().apply {
            getPanelsConfiguration(segmentNumber).map { it.order }.forEach { panelOrder ->
                if (panelOrder != EMPTY_CELL && panelOrder < frameCycle.frames.first().panels.size) {
                    val panelInStringPixels = frameCycle.frames.first().panels[panelOrder].pixels
                    put(
                        panelOrder,
                        bitmapProcessor.convertPanelPixelsToPanelBimap(panelInStringPixels)
                    )
                }
            }
        }
    }

    fun updateAndGetValidatedConfig(newMetaData: PanelMetaData): Set<PanelMetaData> {
        if (newMetaData.order in metaDataSet.map { it.order }) {
            metaDataSet.removeAll { it.order == newMetaData.order }
        }
        metaDataSet.add(newMetaData)
        return validatedConfig()
    }

    fun deleteAndGetValidatedConfig(): Set<PanelMetaData> {
        val currentConfig = frameCycle.configuration.listOfMetaData.toSet()
        var removedPanels = 0
        for (order in currentConfig.map { it.order }.sorted()) {
            val currentPanel = currentConfig.find { it.order == order } ?: continue
            metaDataSet.removeAll { it.order == currentPanel.order }
            if (currentPanel.status == PanelStatus.SELECTED) {
                removedPanels++
            } else {
                metaDataSet.add(
                    currentPanel.copy(order = currentPanel.order - removedPanels)
                )
            }
        }
        return validatedConfig()
    }

    fun checkIfSelectedAreaSquare(): Boolean {
        return checkIfSelectedAreaSquare(metaDataSet)
    }

    fun updateWithUri(uri: Uri, onSuccess: () -> Unit) {
        refreshFramesWithMetaData()
        val mediaType = mediaFileProcessor.getMediaType(uri) ?: return
        scope.launch {
            when (mediaType) {
                MediaFileType.IMAGE -> {
                    mediaFileProcessor.getBitmapFromUri(uri, metaDataSet) { bitmap ->
                        handleBitmapUpdate(bitmap)
                        onSuccess()
                    }
                }

                MediaFileType.GIF -> {
                    mediaFileProcessor.getBitmapCycleFromUri(uri, metaDataSet) { bitmapList, interframeDelay ->
                        refreshFramesWithAmount(bitmapList.size)
                        handleGIFUpdate(bitmapList, interframeDelay)
                        onSuccess()
                    }
                }
            }
        }
    }

    private fun handleBitmapUpdate(bitmap: Bitmap) {
        bitmapProcessor.applyBitmapsToFrameCycle(
            frameCycle = frameCycle,
            type = MediaFileType.IMAGE,
            bitmaps = listOf(bitmap),
        )
        tryToSendConfigurationToMC(frameCycle)
    }

    private fun handleGIFUpdate(bitmapList: List<Bitmap>, interframeDelay: Int) {
        frameCycle.interframeDelay = interframeDelay
        bitmapProcessor.applyBitmapsToFrameCycle(
            frameCycle = frameCycle,
            type = MediaFileType.GIF,
            bitmaps = bitmapList,
        )
        tryToSendConfigurationToMC(frameCycle)
    }

    private fun validatedConfig(): Set<PanelMetaData> {
        val validatedConfig = validate(metaDataSet).toMutableSet()
        return validatedConfig.also { frameCycle.configuration.listOfMetaData = it }
    }

    /** Drawing Level */

    override fun getPanelPixels(panelPosition: Int): MutableList<DrawingColor> {
        refreshFramesWithMetaData()
        val stringPixels = frameCycle.frames.first().panels[panelPosition].pixels
        return stringPixels.map { DrawingColor.getFromString(it) }.toMutableList()
    }

    override fun getPanelPalette(panelPosition: Int): MutableList<DrawingColor> =
        metaDataSet.find { it.order == panelPosition }?.palette
            ?: MutableList(PanelMetaData.PALETTE_SIZE) { DrawingColor(0, 0, 0) }

    override fun updatePixelColorOnPosition(
        panelPosition: Int,
        color: DrawingColor,
        pixelPosition: Int
    ) {
        refreshFramesWithMetaData()
        frameCycle.frames.onEachIndexed { index, frame ->
            frame.panels[panelPosition].pixels[pixelPosition] = color.asString()
        }
        messageHandler.updateConfiguration(metaDataSet)
        messageHandler.sendPixel(
            panelOrder = panelPosition,
            pixelOrder = pixelPosition,
            r = color.r,
            g = color.g,
            b = color.b,
        )
    }

    override fun setPanelPalette(panelPosition: Int, updatedPalette: MutableList<DrawingColor>) {
        refreshFramesWithMetaData()
        metaDataSet.apply {
            val panel = find { it.order == panelPosition } ?: return
            this.remove(panel)
            this.add(panel.copy(palette = updatedPalette))
        }
    }

    /** Utils */

    fun saveStateToDatabase(title: String) {
        scope.launch {
            schemeId?.let { update(it, title) } ?: save(title)
        }
    }

    private suspend fun save(title: String) {
        schemeId = databaseRepository.insertNewScheme(
            UserSavedScheme(
                title = title,
                frameCycle = frameCycle,
            )
        ).toInt()
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

    /* Add empty panel to frames & remove if needed */
    private fun refreshFramesWithMetaData() {
        frameCycle.frames.forEachIndexed { frameIndex, frame ->
            val sizeDifference = metaDataSet.size - frame.panels.size
            val trimmedPanels = when {
                sizeDifference > 0 -> frame.panels + List(sizeDifference) { Panel() }
                sizeDifference < 0 -> frame.panels.dropLast(abs(sizeDifference))
                else -> frame.panels
            }
            frameCycle.frames[frameIndex].panels = trimmedPanels.toMutableList()
        }
    }

    /* Add empty frames & remove if needed */
    private fun refreshFramesWithAmount(framesAmount: Int) {
        val panelsAmount = metaDataSet.size
        val sizeDifference = framesAmount - frameCycle.framesAmount
        val trimmedFrames = when {
            sizeDifference > 0 -> frameCycle.frames + MutableList(sizeDifference) { Frame(MutableList(panelsAmount) { Panel() }) }
            sizeDifference < 0 -> frameCycle.frames.dropLast(abs(sizeDifference))
            else -> frameCycle.frames
        }
        frameCycle.frames = trimmedFrames.toMutableList()
        frameCycle.framesAmount = framesAmount
    }

    private fun tryToSendConfigurationToMC(cycle: FrameCycle) {
        messageHandler.updateConfiguration(cycle.configuration.listOfMetaData)
        messageHandler.sendFrames(cycle)
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