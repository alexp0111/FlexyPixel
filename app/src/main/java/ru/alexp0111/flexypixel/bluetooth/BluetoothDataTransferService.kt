package ru.alexp0111.flexypixel.bluetooth

import android.bluetooth.BluetoothSocket
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.bluetooth.model.TransferResponse
import ru.alexp0111.flexypixel.bluetooth.model.TransferResult
import ru.alexp0111.flexypixel.bluetooth.utils.JsonChecker
import java.io.IOException
import java.io.InputStream

private const val INCOMING_MESSAGE_BYTE_BUFFER_SIZE: Int = 256
private const val TAG = "BluetoothDataTransferService"

class BluetoothDataTransferService(
    private val socket: BluetoothSocket,
) {
    private var inputStream: InputStream = socket.inputStream

    fun listenForIncomingMessages(): Flow<TransferResult> = flow {
        if (!socket.isConnected) {
            return@flow
        }
        val buffer = ByteArray(INCOMING_MESSAGE_BYTE_BUFFER_SIZE)
        val stringCollector = StringBuilder()
        while (true) {
            try {
                val amountOfBytesReceivedInStream = inputStream.read(buffer)
                val stringResult = String(buffer, 0, amountOfBytesReceivedInStream).trim()

                modifyCollectorWithNewData(stringCollector, stringResult)
                val jsonResponse = tryToExtractResponse(stringCollector) ?: continue

                try {
                    val transferResponse = Gson().fromJson(
                        jsonResponse,
                        TransferResponse::class.java,
                    )
                    emit(getResultMessage(transferResponse))
                } catch (e: JsonSyntaxException) {
                    emit(TransferResult.Error(e.message.toString()))
                }
            } catch (e: IOException) {
                break;
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun modifyCollectorWithNewData(stringCollector: StringBuilder, stringResult: String) {
        stringCollector.apply {
            append(stringResult)
            while (isNotEmpty() && !startsWith('{')) {
                deleteAt(0)
            }
        }
    }

    private fun tryToExtractResponse(stringCollector: StringBuilder): String? {
        val result = JsonChecker.extractRightBracketSequence(stringCollector.toString())
        result ?: return null

        stringCollector.deleteRange(0, result.length)
        return result
    }

    private fun getResultMessage(response: TransferResponse): TransferResult {
        return when (response.status) {
            TransferResponse.OK -> TransferResult.TransferSucceeded(TransferResponse.OK)
            TransferResponse.ERROR -> TransferResult.Error(TransferResponse.ERROR)
            else -> TransferResult.Error(TransferResponse.UNCONFIGURED)
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket.outputStream.write(message.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}