package ru.alexp0111.flexypixel.bluetooth

import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.bluetooth.model.TransferResult
import java.io.IOException

private const val INCOMING_MESSAGE_BYTE_BUFFER_SIZE: Int = 1024

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessages(): Flow<TransferResult> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(INCOMING_MESSAGE_BYTE_BUFFER_SIZE)
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    emit(TransferResult.Error("Data transfer failed"))
                    continue
                }

                emit(
                    TransferResult.TransferSucceeded(
                        buffer.decodeToString(endIndex = byteCount)
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket.outputStream.use {
                    it.write(message.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}