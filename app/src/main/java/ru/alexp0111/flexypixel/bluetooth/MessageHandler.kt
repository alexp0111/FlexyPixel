package ru.alexp0111.flexypixel.bluetooth

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.bluetooth.model.TransferResult
import ru.alexp0111.flexypixel.bluetooth.utils.MessageConverter
import ru.alexp0111.flexypixel.util.PermissionResolver
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MessageHandler"
private const val CONFIGURATION_SIZE = 9

@Singleton
class MessageHandler @Inject constructor(
    private val controller: AndroidBluetoothController,
    private val permissionResolver: PermissionResolver,
) {

    private var configuration: PanelConfiguration? = null
    private var mode: TransactionMode? = null
    private val messageQueue: LinkedList<BluetoothMessage> = LinkedList()

    private var _errors = MutableSharedFlow<String>(replay = 1)
    val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val listener = CoroutineScope(Dispatchers.IO).launch {
        controller.incomingMessages.collect {
            when (it) {
                is TransferResult.TransferSucceeded -> {
                    if (messageQueue.isNotEmpty()) {
                        popMessageQueue()
                    }
                }

                is TransferResult.Error -> {
                    _errors.tryEmit(it.errorMessage)
                }

                else -> Unit
            }
        }
    }

    private fun popMessageQueue() {
        if (messageQueue.isNotEmpty()) {
            val message = messageQueue.pop()
            controller.sendMessage(message.asJson())
            Log.d(TAG, message.asJson())
        }
    }

    fun sendPixel(
        panelOrder: Int,
        pixelOrder: Int,
        rChannel: Int,
        gChannel: Int,
        bChannel: Int,
    ) {
        if (mode == null || mode != TransactionMode.PIXEL) {
            mode = TransactionMode.PIXEL
            messageQueue.add(MessageType.MODE, TransactionMode.PIXEL)
        }
        val message = MessagePixel(
            panelPosition = panelOrder,
            pixelPosition = MessageConverter.convert(pixelOrder),
            pixelColor = rChannel.toString() + gChannel.toString() + bChannel.toString()
        )
        messageQueue.add(MessageType.DATA, message)
        popMessageQueue()
    }

    fun updateConfiguration(numOf64Panels: Int) {
        val curConfig = Array(CONFIGURATION_SIZE) { "064" }
        for (i in numOf64Panels until CONFIGURATION_SIZE) {
            curConfig[i] = "000"
        }
        val incomingPanelConfiguration = PanelConfiguration(curConfig)
        updateConfiguration(incomingPanelConfiguration)
    }

    fun updateConfiguration(configuration: Array<String>) {
        updateConfiguration(PanelConfiguration(configuration))
    }

    private fun updateConfiguration(incomingPanelConfiguration: PanelConfiguration) {
        if (configuration == null || configuration != incomingPanelConfiguration) {
            configuration = incomingPanelConfiguration.also {
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

    //Log.d(TAG, Pair(type, message).toString())
}
