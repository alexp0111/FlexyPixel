package ru.alexp0111.bt_service.business

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.alexp0111.bt_service.AndroidBluetoothController
import ru.alexp0111.bt_service.model.message.BluetoothMessage
import ru.alexp0111.bt_service.model.message.MessageFrame
import ru.alexp0111.bt_service.model.message.MessagePanelConfiguration
import ru.alexp0111.bt_service.model.message.MessageTransactionMode
import ru.alexp0111.bt_service.model.message.MessageType
import ru.alexp0111.bt_service.model.message.TestMessage
import ru.alexp0111.bt_service.model.message.converter.IMessageConverter
import ru.alexp0111.bt_service.model.transfer.TransferResponse
import ru.alexp0111.bt_service.model.transfer.TransferResult
import ru.alexp0111.core.di.AppScope
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.PanelMetaData
import java.util.LinkedList
import javax.inject.Inject

private const val TAG = "MessageHandler"

@AppScope
class MessageHandler @Inject constructor(
    private val controller: AndroidBluetoothController,
    private val messageConverter: IMessageConverter,
) : IMessageHandler {
    @get:Synchronized
    @set:Synchronized
    private var isWaitingForResponse = false

    @get:Synchronized
    @set:Synchronized
    private var lastSentMessage: BluetoothMessage? = null

    @get:Synchronized
    @set:Synchronized
    private var lastSentMessageType: MessageType? = null

    private var configuration: MessagePanelConfiguration? = null
    private var mode: MessageTransactionMode? = null
    private val messageQueue: LinkedList<BluetoothMessage> = LinkedList()

    private var _errors = MutableSharedFlow<String>(replay = 1)
    private var _successResponse = MutableSharedFlow<String>(replay = 1)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            controller.incomingMessages.collect {
                isWaitingForResponse = false
                when (it) {
                    is TransferResult.TransferSucceeded -> {
                        _successResponse.tryEmit(it.message)
                        lastSentMessage = null
                        tryPopMessageQueue()
                    }

                    is TransferResult.Error -> {
                        handleError(it.errorMessage)
                        _errors.tryEmit(it.errorMessage)
                    }
                }
            }
        }
    }

    private fun handleError(errorMessage: String) {
        retryConfigIfNeeded(errorMessage)
        retryLastSentMessage() ?: return
        tryPopMessageQueue()
    }

    private fun retryConfigIfNeeded(errorMessage: String) {
        if (errorMessage != TransferResponse.UNCONFIGURED) return
        mode?.let {
            messageQueue.push(MessageType.MODE, it)
        }
    }

    private fun retryLastSentMessage(): BluetoothMessage? {
        val message = lastSentMessage ?: return null
        val type = lastSentMessageType ?: return null
        messageQueue.push(type, message)
        return message
    }

    private fun tryPopMessageQueue() {
        if (messageQueue.isNotEmpty() && !isWaitingForResponse) {
            isWaitingForResponse = true
            val message = messageQueue.pop()
            saveAsLastSentMessage(message)
            controller.sendMessage(message.asJson())
        }
    }

    private fun saveAsLastSentMessage(message: BluetoothMessage) {
        when (message) {
            is MessageType -> lastSentMessageType = message
            else -> lastSentMessage = message
        }
    }

    override fun sendPixel(panelOrder: Int, pixelOrder: Int, r: Int, g: Int, b: Int) {
        changeModeIfNeeded(MessageTransactionMode.PIXEL)

        messageQueue.add(
            type = MessageType.DATA,
            message = messageConverter.convertPixelToMessage(panelOrder, pixelOrder, r, g, b)
        )
        tryPopMessageQueue()
    }

    override fun sendFrames(frameCycle: FrameCycle) {
        val messageFrames = messageConverter.convertFramesToMessage(frameCycle)
        sendMessageFrames(messageFrames, frameCycle.interframeDelay)
    }

    private fun sendMessageFrames(frames: List<MessageFrame>, interframeDelay: Int) {
        changeModeIfNeeded(MessageTransactionMode.SEQUENCE)
        configuration?.let {
            messageQueue.add(MessageType.CONFIG, it)
        }

        val metaDataMessage = messageConverter.convertFramesToMetaDataMessage(frames, interframeDelay)
        messageQueue.add(MessageType.DATA, metaDataMessage)

        frames.forEach {
            messageQueue.add(it)
        }

        tryPopMessageQueue()
    }

    private fun changeModeIfNeeded(transactionMode: MessageTransactionMode) {
        if (mode == null || mode != transactionMode) {
            mode = transactionMode
            messageQueue.add(MessageType.MODE, transactionMode)
        }
    }

    override fun updateConfiguration(configuration: Set<PanelMetaData>) {
        val panelMetaDataMessage = messageConverter.convertPanelMetaDataToMessage(configuration)
        updateConfiguration(panelMetaDataMessage)
    }

    private fun updateConfiguration(incomingMessagePanelConfiguration: MessagePanelConfiguration) {
        if (configuration == null || configuration != incomingMessagePanelConfiguration) {
            configuration = incomingMessagePanelConfiguration.also {
                messageQueue.add(MessageType.CONFIG, it)
            }
        }
    }

    override fun sendTestMessage(message: String) {
        messageQueue.add(TestMessage(message))
    }

    override fun saveCurrentState() {
        messageQueue.add(MessageType.SAVE)
    }

    override fun clearCurrentState() {
        messageQueue.add(MessageType.RSTP)
    }

    override fun getErrorFlow(): SharedFlow<String> = _errors.asSharedFlow()

    override fun getResponseFlow(): SharedFlow<String> = _successResponse.asSharedFlow()
}

fun LinkedList<BluetoothMessage>.add(
    type: MessageType,
    message: BluetoothMessage,
) {
    this.add(type)
    this.add(message)
}

fun LinkedList<BluetoothMessage>.push(
    type: MessageType,
    message: BluetoothMessage,
) {
    this.push(message)
    this.push(type)
}
