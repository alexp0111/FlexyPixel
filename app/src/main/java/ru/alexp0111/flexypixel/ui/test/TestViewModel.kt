package ru.alexp0111.flexypixel.ui.test

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.launch
import ru.alexp0111.bt_service.business.IMessageHandler
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.Frame
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.Panel
import ru.alexp0111.core.model.PanelConfiguration
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.core.model.PanelOrientation
import ru.alexp0111.core.model.PanelStatus
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.media_loader.IMediaFilesProcessor
import ru.alexp0111.model.MediaFileType
import ru.alexp0111.flexypixel.ui.test.model.TestFramesModel
import ru.alexp0111.flexypixel.ui.test.model.TestIntent
import ru.alexp0111.flexypixel.ui.test.model.TestPixelModel
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessage
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessageType
import ru.alexp0111.flexypixel.ui.test.model.TestUiState
import javax.inject.Inject

internal class TestViewModel @Inject constructor(
    private val mediaFileProcessor: IMediaFilesProcessor,
    private val bitmapProcessor: IBitmapProcessor,
    private val messageHandler: IMessageHandler,
) : MVIViewModel<TestIntent, TestUiState, Nothing>() {

    init {
        launch {
            messageHandler.getErrorFlow().collect {
                val terminalList = uiState.value.terminalMessages
                val newMessage = TestTerminalMessage(
                    text = it,
                    type = TestTerminalMessageType.RESPONSE_ERROR,
                )
                setState { copy(terminalMessages = terminalList + newMessage) }
            }
        }
        launch {
            messageHandler.getResponseFlow().collect {
                val terminalList = uiState.value.terminalMessages
                val newMessage = TestTerminalMessage(
                    text = it,
                    type = TestTerminalMessageType.RESPONSE_SUCCESS,
                )
                setState { copy(terminalMessages = terminalList + newMessage) }
            }
        }
    }

    override fun setInitialState(): TestUiState =
        TestUiState(
            totalAmountOfPanels = null,
            testPixel = TestPixelModel(),
            testFrames = TestFramesModel(),
            terminalInput = "",
            terminalMessages = emptyList(),
        )

    override fun handleIntent(intent: TestIntent) =
        when (intent) {
            is TestIntent.SendConfig -> onSendConfig(intent.totalPanels)
            is TestIntent.SendPixel -> onSendPixel(intent.testPixelModel)
            is TestIntent.SendFrames -> onSendFrames(intent.testFramesModel)
            is TestIntent.PickMedia -> onPickMedia(intent.uri)
            TestIntent.SendMessage -> onSendMessage()
            is TestIntent.TerminalInputChanged -> onTerminalInputChanged(intent.input)
            is TestIntent.TerminalMessageClicked -> onTerminalMessageClicked(intent.message)
        }


    private fun onSendConfig(totalPanels: Int?) {
        val amountOfPanels = minOf(totalPanels ?: 0, CommonSizeConstants.PANELS_TOTAL_AMOUNT)
        messageHandler.updateConfiguration(getPanelConfiguration(amountOfPanels).listOfMetaData)
        setState { copy(totalAmountOfPanels = amountOfPanels) }
    }

    private fun onSendPixel(testPixelModel: TestPixelModel) {
        messageHandler.sendPixel(
            panelOrder = testPixelModel.panel ?: 0,
            pixelOrder = testPixelModel.pixel ?: 0,
            orientation = PanelOrientation.LEFT,
            r = testPixelModel.r ?: 0,
            g = testPixelModel.g ?: 0,
            b = testPixelModel.b ?: 0,
        )
        setState { copy(testPixel = testPixelModel) }
    }

    private fun onSendFrames(testFramesModel: TestFramesModel) {
        val amountOfPanels = minOf(uiState.value.totalAmountOfPanels ?: 0, CommonSizeConstants.PANELS_TOTAL_AMOUNT)
        messageHandler.sendFrames(
            FrameCycle(
                configuration = getPanelConfiguration(amountOfPanels),
                framesAmount = testFramesModel.framesAmount ?: 0,
                interframeDelay = testFramesModel.interframeDelay ?: 1000,
                frames = getTestFrames(testFramesModel.framesAmount ?: 0).toMutableList()
            )
        )
        setState { copy(testFrames = testFramesModel) }
    }

    private fun onTerminalInputChanged(input: String) {
        setState { copy(terminalInput = input) }
    }

    private fun onSendMessage() {
        messageHandler.sendTestMessage(uiState.value.terminalInput)
        val terminalList = uiState.value.terminalMessages
        val newMessage = TestTerminalMessage(
            text = uiState.value.terminalInput,
            type = TestTerminalMessageType.REQUEST,
        )
        setState { copy(terminalMessages = terminalList + newMessage) }
    }

    private fun onTerminalMessageClicked(message: TestTerminalMessage) {
        setState { copy(terminalInput = message.text) }
    }

    private fun onPickMedia(uri: Uri) {
        val amountOfPanels = minOf(uiState.value.totalAmountOfPanels ?: 0, CommonSizeConstants.PANELS_TOTAL_AMOUNT)
        val mediaType = mediaFileProcessor.getMediaType(uri) ?: return
        launch {
            when (mediaType) {
                MediaFileType.IMAGE -> mediaFileProcessor.getBitmapFromUri(
                    uri = uri,
                    metaData = setOf(PanelMetaData(absoluteX = 0, absoluteY = 0, status = PanelStatus.SELECTED)),
                    onBitmapExtracted = { handleBitmapUpdate(amountOfPanels, it) }
                )

                MediaFileType.GIF -> mediaFileProcessor.getBitmapCycleFromUri(
                    uri = uri,
                    metaData = setOf(PanelMetaData(absoluteX = 0, absoluteY = 0, status = PanelStatus.SELECTED)),
                    onBitmapCycleExtracted = { list, delay -> handleGIFUpdate(amountOfPanels, list, delay) }
                )
            }
        }
    }

    private fun handleBitmapUpdate(amountOfPanels: Int, bitmap: Bitmap) {
        val pixelBitmap = bitmapProcessor.convertBitmapToPixelStringMatrix(bitmap)
        val frame = applyPixelBitmapForFrame(amountOfPanels, pixelBitmap)
        messageHandler.sendFrames(
            FrameCycle(
                configuration = getPanelConfiguration(amountOfPanels),
                framesAmount = 1,
                interframeDelay = 1000,
                frames = mutableListOf(frame)
            )
        )
    }

    private fun handleGIFUpdate(amountOfPanels: Int, bitmapList: List<Bitmap>, interframeDelay: Int) {
        val pixelBitmapList = bitmapList.map { bitmapProcessor.convertBitmapToPixelStringMatrix(it) }
        val frames = pixelBitmapList.map { applyPixelBitmapForFrame(amountOfPanels, it) }
        messageHandler.sendFrames(
            FrameCycle(
                configuration = getPanelConfiguration(amountOfPanels),
                framesAmount = frames.size,
                interframeDelay = interframeDelay,
                frames = frames.toMutableList()
            )
        )
    }

    private fun applyPixelBitmapForFrame(amountOfPanels: Int, array: Array<Array<String>>): Frame {
        val frame = Frame(MutableList(amountOfPanels) { Panel(Array(PanelMetaData.TYPE_64) { "000" }) })
        frame.panels.forEach { panel ->
            var counter = 0
            for (row in 0..array.lastIndex) {
                for (column in 0..array.first().lastIndex) {
                    panel.pixels[counter] = array[row][column]
                    counter++
                }
            }
        }
        return frame
    }

    private fun getPanelConfiguration(amountOfPanels: Int): PanelConfiguration {
        val config = mutableSetOf<PanelMetaData>().apply {
            for (index in 0..<amountOfPanels) {
                add(PanelMetaData(order = index, absoluteX = 0, absoluteY = 0))
            }
        }
        return PanelConfiguration(config)
    }

    private fun getTestFrames(numberOfFrames: Int): List<Frame> {
        val amountOfPanels = minOf(uiState.value.totalAmountOfPanels ?: 0, CommonSizeConstants.PANELS_TOTAL_AMOUNT)
        val framesList = List(numberOfFrames) {
            Frame(MutableList(amountOfPanels) { Panel(Array(PanelMetaData.TYPE_64) { "000" }) })
        }
        var cycleColor = getRandomColor()
        framesList.forEachIndexed { frameIndex, frame ->
            val firstPixels = frameIndex % PanelMetaData.TYPE_64
            if (firstPixels == 0) cycleColor = getRandomColor()
            frame.panels.forEach { panel ->
                for (pixel in 0..firstPixels) panel.pixels[pixel] = cycleColor
            }
        }
        return framesList
    }

    private fun getRandomColor() = buildString { repeat(3) { append((0..9).random()) } }

}