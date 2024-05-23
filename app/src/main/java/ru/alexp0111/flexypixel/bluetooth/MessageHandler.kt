package ru.alexp0111.flexypixel.bluetooth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.bluetooth.model.TransferResponse
import ru.alexp0111.flexypixel.bluetooth.model.TransferResult
import ru.alexp0111.flexypixel.bluetooth.utils.MessageConverter
import ru.alexp0111.flexypixel.data.model.Frame
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import java.lang.StringBuilder
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MessageHandler"

@Singleton
class MessageHandler @Inject constructor(
    private val controller: AndroidBluetoothController,
) {
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
    val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            controller.incomingMessages.collect {
                isWaitingForResponse = false
                when (it) {
                    is TransferResult.TransferSucceeded -> {
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

    private fun retryLastSentMessage(): BluetoothMessage? {
        val message = lastSentMessage ?: return null
        val type = lastSentMessageType ?: return null
        messageQueue.push(type, message)
        return message
    }

    private fun retryConfigIfNeeded(errorMessage: String) {
        if (errorMessage != TransferResponse.UNCONFIGURED) return
        mode?.let {
            messageQueue.push(MessageType.MODE, it)
        }
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

    fun sendPixel(
        panelOrder: Int,
        pixelOrder: Int,
        rChannel: Int,
        gChannel: Int,
        bChannel: Int,
    ) {
        changeModeIfNeeded(MessageTransactionMode.PIXEL)

        val message = MessagePixel(
            panelPosition = panelOrder,
            pixelPosition = MessageConverter.convert(pixelOrder),
            pixelColor = rChannel.toString() + gChannel.toString() + bChannel.toString()
        )

        messageQueue.add(MessageType.DATA, message)
        tryPopMessageQueue()
    }

    fun sendFrames(frames: MutableList<Frame>, interframeDelay: Int) {
        val messageFrames = frames.map {
            it.toMessageFrame()
        }.toList()
        sendMessageFrames(messageFrames, interframeDelay)
    }

    fun sendMessageFrames(frames: List<MessageFrame>, interframeDelay: Int) {
        changeModeIfNeeded(MessageTransactionMode.SEQUENCE)
        configuration?.let {
            messageQueue.add(MessageType.CONFIG, it)
        }

        val message = MessageFramesMetaData(
            MessageConverter.convert(frames.size),
            MessageConverter.convert(interframeDelay, MessageFramesMetaData.INTERFRAME_SIZE),
        )

        messageQueue.add(MessageType.DATA, message)
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

    fun updateConfiguration(configuration: List<PanelMetaData>) {
        val stringBuilder = StringBuilder().apply {
            configuration.forEach {
                append(MessageConverter.convert(it.type, 3))
            }
            while (this.length != PanelConfiguration.MAX_SIZE*3) {
                append("000")
            }
        }
        updateConfiguration(MessagePanelConfiguration(stringBuilder.toString()))
    }

    fun updateConfiguration(configuration: Array<String>) {
        updateConfiguration(MessagePanelConfiguration(configuration.joinToString("")))
    }

    private fun updateConfiguration(incomingMessagePanelConfiguration: MessagePanelConfiguration) {
        if (configuration == null || configuration != incomingMessagePanelConfiguration) {
            configuration = incomingMessagePanelConfiguration.also {
                messageQueue.add(MessageType.CONFIG, it)
            }
        }
    }
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
