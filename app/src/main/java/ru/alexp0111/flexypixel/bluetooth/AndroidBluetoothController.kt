package ru.alexp0111.flexypixel.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import ru.alexp0111.flexypixel.bluetooth.model.ConnectionResult
import ru.alexp0111.flexypixel.bluetooth.model.FlexyPixelDevice
import ru.alexp0111.flexypixel.bluetooth.model.TransferResult
import ru.alexp0111.flexypixel.bluetooth.receivers.AvailableDevicesReceiver
import ru.alexp0111.flexypixel.bluetooth.receivers.BluetoothStateReceiver
import ru.alexp0111.flexypixel.util.PermissionResolver
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AndroidBluetoothController"
private const val DEVICE_NAME_REGEX = "Flexy Pixel.*"
private const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"

@SuppressLint("MissingPermission")
@Singleton
class AndroidBluetoothController @Inject constructor(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
    private val permissionResolver: PermissionResolver,
) {
    private var dataTransferService: BluetoothDataTransferService? = null
    private var currentClientSocket: BluetoothSocket? = null

    private val _isConnected = MutableStateFlow(Pair(null as FlexyPixelDevice?, false))
    val isConnected: StateFlow<Pair<FlexyPixelDevice?, Boolean>>
        get() = _isConnected.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<FlexyPixelDevice>>(emptyList())
    val scannedDevices: StateFlow<List<FlexyPixelDevice>>
        get() = _scannedDevices.asStateFlow()

    private var _incomingMessages = MutableSharedFlow<TransferResult>(replay = 1)
    val incomingMessages: SharedFlow<TransferResult>
        get() = _incomingMessages.asSharedFlow()

    private val foundDeviceReceiver = AvailableDevicesReceiver { bluetoothDevice ->
        val flexyPixelDevice = FlexyPixelDevice.from(bluetoothDevice)
        _scannedDevices.update { devices ->
            if (flexyPixelDevice in devices) devices else devices + flexyPixelDevice
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        if (!isDeviceAvailable(bluetoothDevice)) {
            return@BluetoothStateReceiver
        }
        /**
         * TODO: We should inform user if device disconnected in a workflow
         * */
        _isConnected.update {
            Pair(
                FlexyPixelDevice.from(bluetoothDevice),
                isConnected,
            )
        }
    }

    private fun isDeviceAvailable(bluetoothDevice: BluetoothDevice): Boolean {
        val pairedDevices = bluetoothAdapter.bondedDevices ?: return false
        val flexyPixelDevice = FlexyPixelDevice.from(bluetoothDevice)
        return pairedDevices.contains(bluetoothDevice) && flexyPixelDevice in scannedDevices.value
    }

    fun isConnectedToFPModule(): Flow<Boolean> = flow {
        val pairedDevice = bluetoothAdapter.bondedDevices.filter {
            it.name.matches(DEVICE_NAME_REGEX.toRegex())
        }
        if (pairedDevice.isEmpty() || pairedDevice.size != 1) {
            emit(false)
        } else {
            setUpConnection(FlexyPixelDevice.from(pairedDevice.first())).collect {
                emit(it is ConnectionResult.ConnectionEstablished)
            }
        }
    }

    fun startDiscovery() {
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            return
        }

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND),
        )
        bluetoothAdapter.startDiscovery()
    }

    fun setUpConnection(device: FlexyPixelDevice): Flow<ConnectionResult> {
        return flow {
            if (!permissionResolver.isBluetoothPermissionsGranted()) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            val remoteDevice = bluetoothAdapter.getRemoteDevice(device.address)
            currentClientSocket = remoteDevice?.createRfcommSocketToServiceRecord(
                UUID.fromString(SERVICE_UUID)
            )
            stopDiscovery()

            currentClientSocket?.let { socket ->
                try {
                    socket.connect()
                    registerBluetoothStateReceiver()
                    emit(ConnectionResult.ConnectionEstablished)
                    BluetoothDataTransferService(socket).apply {
                        dataTransferService = this
                        _incomingMessages.emitAll(
                            listenForIncomingMessages()
                        )
                    }
                } catch (e: IOException) {
                    socket.close()
                    emit(ConnectionResult.Error("Connection was interrupted"))
                    currentClientSocket = null
                }
            }
        }.catch { throwable ->
            Log.d(TAG, throwable.message.toString())
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    private fun registerBluetoothStateReceiver() {
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }

    fun sendMessage(message: String) {
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            return
        }
        dataTransferService?.sendMessage(message)
    }

    fun stopDiscovery() {
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            return
        }
        bluetoothAdapter.cancelDiscovery()
        try {
            context.unregisterReceiver(foundDeviceReceiver)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, e.message.toString())
        }
    }

    fun closeConnection() {
        currentClientSocket?.close()
        currentClientSocket = null
        dataTransferService = null
    }

    fun release() {
        closeConnection()
        try {
            context.unregisterReceiver(bluetoothStateReceiver)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, e.message.toString())
        }
    }
}