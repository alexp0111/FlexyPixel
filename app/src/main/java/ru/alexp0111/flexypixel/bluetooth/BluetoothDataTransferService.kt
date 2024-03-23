package ru.alexp0111.flexypixel.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
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

    fun listenForIncomingMessages(): Flow<TransferResult> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(INCOMING_MESSAGE_BYTE_BUFFER_SIZE)
            val stringCollector = StringBuilder()
            while (true) {
                try {
                    val byteCount = inputStream.read(buffer)
                    val stringResult = String(buffer, 0, byteCount)

                    if (stringResult.trim().isNotEmpty()) {
                        Log.d(TAG, stringResult) // FIXME: DEBUG
                        stringCollector.append(stringCollector)
                    } else {
                        continue
                    }

                    val jsonResponse = tryToExtractResponse(stringCollector.toString()) ?: continue
                    stringCollector.removePrefix(jsonResponse)

                    // FIXME: DEBUG
                    if (stringCollector.isEmpty()) {
                        Log.d(TAG, "Response fully collected")
                    }
                    // FIXME: DEBUG

                    val response = try {
                        Gson().fromJson(jsonResponse, TransferResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        emit(TransferResult.Error(e.message.toString()))
                        continue
                    }
                    emit(getResultMessage(response))
                } catch (e: IOException) {
                    break;
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getResultMessage(response: TransferResponse): TransferResult {
        return when (response.status) {
            TransferResponse.OK -> TransferResult.TransferSucceeded(TransferResponse.OK)
            TransferResponse.ERROR -> TransferResult.Error(TransferResponse.ERROR)
            else -> TransferResult.Error(TransferResponse.UNCONFIGURED)
        }
    }

    private fun tryToExtractResponse(collectedResult: String): String? {
        return JsonChecker.extractRightBracketSequence(collectedResult)
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