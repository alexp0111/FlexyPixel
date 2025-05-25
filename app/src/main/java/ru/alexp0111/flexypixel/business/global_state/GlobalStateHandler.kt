package ru.alexp0111.flexypixel.business.global_state

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.alexp0111.bt_service.business.IMessageHandler
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.Frame
import ru.alexp0111.core.model.main.FrameCycle
import ru.alexp0111.core.model.main.Panel
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.core.model.main.PanelOrientation
import ru.alexp0111.core.model.main.PanelStatus
import ru.alexp0111.flexypixel.business.global_state.convertor.IApiConfigurationConverter
import ru.alexp0111.flexypixel.business.global_state.panel_validation.IPanelPositionValidator
import ru.alexp0111.local_repo.database.schemes.SavedSchemeRepository
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.media_loader.IMediaFilesProcessor
import ru.alexp0111.model.MediaFileType
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelGlobalStateHolder
import ru.alexp0111.flexypixel.ui.drawing.DrawingGlobalStateHolder
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelGlobalStateHolder
import ru.alexp0111.remote_repo.scheme.repository.ISchemeRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@AssistedFactory
internal interface GlobalStateHandlerFactory {
    fun create(schemeId: String?): GlobalStateHandler
}

private const val TAG = "GlobalStateHandler"
const val EMPTY_CELL = -1

@OptIn(ExperimentalCoroutinesApi::class)
internal class GlobalStateHandler @AssistedInject constructor(
    private val databaseRepository: SavedSchemeRepository,
    private val schemeRepository: ISchemeRepository,
    private val messageHandler: IMessageHandler,
    private val bitmapProcessor: IBitmapProcessor,
    private val mediaFileProcessor: IMediaFilesProcessor,
    private val panelValidator: IPanelPositionValidator,
    private val schemeConvertor: IApiConfigurationConverter,
    @Assisted var schemeId: String?,
) : UpperAbstractionLevelGlobalStateHolder,
    DrawingGlobalStateHolder,
    IPanelPositionValidator by panelValidator,
    DisplayLevelGlobalStateHolder {

    private lateinit var frameCycle: Deferred<FrameCycle>
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(TAG, throwable.message.toString())
    }
    private val scope = CoroutineScope(Dispatchers.IO + errorHandler)

    private val metaDataSet: MutableSet<PanelMetaData>
        get() = frameCycle.getCompleted().configuration.listOfMetaData

    init {
        Log.d(TAG, "in init block")
        scope.launch {
            val id = schemeId
            messageHandler.clearCurrentState()
            frameCycle = async {
                if (id == null) {
                    FrameCycle.getNewInstance()
                } else {
                    // val cycle = databaseRepository.getSchemeById(id).frameCycle
                    val cycle = schemeRepository.getConfigById(id)
                        ?.let(schemeConvertor::convertApiToCycle)
                        ?: FrameCycle.getNewInstance()
                    tryToSendConfigurationToMC(cycle)
                    cycle
                }
            }
        }
    }

    /** Upper Abstraction Level */

    override suspend fun getSegmentsBitmapImages(size: Int): List<Bitmap?> {
        frameCycle.await()
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

    override fun getPanelsImages(segmentNumber: Int): MutableMap<Int, List<DrawingColor>?> {
        return mutableMapOf<Int, List<DrawingColor>?>().apply {
            getPanelsConfiguration(segmentNumber).map { it.order }.forEach { panelOrder ->
                if (panelOrder != EMPTY_CELL && panelOrder < frameCycle.getCompleted().frames.first().panels.size) {
                    val panelInStringPixels = frameCycle.getCompleted().frames.first().panels[panelOrder].pixels
                    put(
                        panelOrder,
                        panelInStringPixels.map(DrawingColor::getFromString)
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
        val currentConfig = frameCycle.getCompleted().configuration.listOfMetaData.toSet()
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
            frameCycle = frameCycle.getCompleted(),
            type = MediaFileType.IMAGE,
            bitmaps = listOf(bitmap),
        )
        tryToSendConfigurationToMC(frameCycle.getCompleted())
    }

    private fun handleGIFUpdate(bitmapList: List<Bitmap>, interframeDelay: Int) {
        frameCycle.getCompleted().interframeDelay = interframeDelay
        bitmapProcessor.applyBitmapsToFrameCycle(
            frameCycle = frameCycle.getCompleted(),
            type = MediaFileType.GIF,
            bitmaps = bitmapList,
        )
        tryToSendConfigurationToMC(frameCycle.getCompleted())
    }

    private fun validatedConfig(): Set<PanelMetaData> {
        val validatedConfig = validate(metaDataSet).toMutableSet()
        return validatedConfig.also { frameCycle.getCompleted().configuration.listOfMetaData = it }
    }

    /** Drawing Level */

    override fun getPanelPixels(panelPosition: Int): MutableList<DrawingColor> {
        refreshFramesWithMetaData()
        val stringPixels = frameCycle.getCompleted().frames.first().panels[panelPosition].pixels
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
        frameCycle.getCompleted().frames.onEachIndexed { index, frame ->
            frame.panels[panelPosition].pixels[pixelPosition] = color.asString()
        }
        val orientation = metaDataSet.find { it.order == panelPosition }?.orientation ?: PanelOrientation.LEFT
        messageHandler.updateConfiguration(metaDataSet)
        messageHandler.sendPixel(
            panelOrder = panelPosition,
            orientation = orientation,
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
        val scheme = UserSavedScheme(
            title = title,
            createdAt = getFormattedDate(),
            preview = null,
            frameCycle = frameCycle.getCompleted(),
        )
        databaseRepository.insertNewScheme(scheme).also {
            schemeId = scheme.id
        }
    }

    private fun getFormattedDate(): String {
        val date = Calendar.getInstance().time
        val mobileFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return mobileFormat.format(date)
    }

    private suspend fun update(schemeId: String, title: String) {
        val scheme = databaseRepository.getSchemeById(schemeId)
        databaseRepository.updateExistingScheme(
            UserSavedScheme(
                id = schemeId,
                title = title,
                createdAt = scheme.createdAt,
                preview = scheme.preview,
                frameCycle = frameCycle.getCompleted(),
            )
        )
    }

    /* Add empty panel to frames & remove if needed */
    private fun refreshFramesWithMetaData() {
        frameCycle.getCompleted().frames.forEachIndexed { frameIndex, frame ->
            val sizeDifference = metaDataSet.size - frame.panels.size
            val trimmedPanels = when {
                sizeDifference > 0 -> frame.panels + List(sizeDifference) { Panel() }
                sizeDifference < 0 -> frame.panels.dropLast(abs(sizeDifference))
                else -> frame.panels
            }
            frameCycle.getCompleted().frames[frameIndex].panels = trimmedPanels.toMutableList()
        }
    }

    /* Add empty frames & remove if needed */
    private fun refreshFramesWithAmount(framesAmount: Int) {
        val panelsAmount = metaDataSet.size
        val sizeDifference = framesAmount - frameCycle.getCompleted().framesAmount
        val trimmedFrames = when {
            sizeDifference > 0 -> frameCycle.getCompleted().frames + MutableList(sizeDifference) { Frame(MutableList(panelsAmount) { Panel() }) }
            sizeDifference < 0 -> frameCycle.getCompleted().frames.dropLast(abs(sizeDifference))
            else -> frameCycle.getCompleted().frames
        }
        frameCycle.getCompleted().frames = trimmedFrames.toMutableList()
        frameCycle.getCompleted().framesAmount = framesAmount
    }

    private fun tryToSendConfigurationToMC(cycle: FrameCycle) {
        messageHandler.updateConfiguration(cycle.configuration.listOfMetaData)
        messageHandler.sendFrames(cycle)
    }

    companion object {
        private var instance: GlobalStateHandler? = null
        fun getInstance(factory: GlobalStateHandlerFactory, schemeId: String?): GlobalStateHandler {
            return instance ?: factory.create(schemeId).also {
                instance = it
            }
        }

        fun reset() {
            instance = null
        }
    }
}