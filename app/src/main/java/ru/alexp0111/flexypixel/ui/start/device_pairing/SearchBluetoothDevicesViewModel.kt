package ru.alexp0111.flexypixel.ui.start.device_pairing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.alexp0111.flexypixel.bluetooth.AndroidBluetoothController
import ru.alexp0111.flexypixel.bluetooth.model.ConnectionResult
import ru.alexp0111.flexypixel.bluetooth.model.FlexyPixelDevice
import ru.alexp0111.flexypixel.navigation.Screens
import javax.inject.Inject

private const val TAG = "SearchBluetoothDevicesViewModel"

class SearchBluetoothDevicesViewModel @Inject constructor(
    private val controller: AndroidBluetoothController,
    private val router: Router,
) : ViewModel(), ActionConsumer, StateHolder {

    private val _actions: MutableSharedFlow<Action> by lazy {
        MutableSharedFlow(extraBufferCapacity = 1)
    }
    private val _uiState = MutableStateFlow(UiState())
    override val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _actions.collect { action ->
                val currentState = _uiState.value
                _uiState.value = reduce(state = currentState, action = action)
                viewModelScope.launch {
                    dispatchAction(state = currentState, action = action)
                }
            }
        }
    }

    override fun consumeAction(action: Action) {
        _actions.tryEmit(action)
    }

    private fun dispatchAction(state: UiState, action: Action) {
        when (action) {
            is Action.LoadAvailableDevices -> loadBluetoothDevices()
            is Action.AvailableDevicesUpdated -> Unit
            is Action.StopLoadingBluetoothDevices -> controller.stopDiscovery()

            is Action.ConnectBluetoothDevice -> connectBluetoothDevice(action.device)
            is Action.ConnectionInProgress -> Unit
            is Action.ConnectionSucceed -> router.newRootScreen(Screens.MenuScreen())

            is Action.ConnectionFailed -> resetConnectedDevice()
            is Action.NotifyErrorShown -> Unit

            is Action.CloseConnection -> controller.closeConnection()
            is Action.ConnectionClosed -> resetConnectedDevice()
        }
    }

    private fun resetConnectedDevice() {
        _uiState.update {
            it.copy(connectedDevice = null)
        }
    }

    private fun loadBluetoothDevices() {
        controller.startDiscovery()
        viewModelScope.plus(Dispatchers.IO).launch {
            controller.scannedDevices.collect {
                consumeAction(
                    Action.AvailableDevicesUpdated(
                        it.map { flexyPixelDevice ->
                            BluetoothDeviceState.from(flexyPixelDevice)
                        }
                    )
                )
            }
        }
    }

    private fun connectBluetoothDevice(device: BluetoothDeviceState) {
        viewModelScope.plus(Dispatchers.IO).launch {
            consumeAction(Action.ConnectionInProgress(device))
            controller.setUpConnection(FlexyPixelDevice.from(device)).collect {
                val action = when (it) {
                    is ConnectionResult.ConnectionEstablished -> {
                        Action.ConnectionSucceed(device)
                    }

                    is ConnectionResult.Error -> {
                        Action.ConnectionFailed(device, it.errorMessage)
                    }
                }
                consumeAction(action)
            }
        }

        viewModelScope.plus(Dispatchers.IO).launch {
            controller.isConnected.collect {
                val connectedDevice = it.first ?: return@collect
                if (connectedDevice.address != FlexyPixelDevice.from(device).address) {
                    return@collect
                }
                val isConnected = it.second
                if (isConnected) {
                    consumeAction(
                        Action.ConnectionSucceed(
                            BluetoothDeviceState.from(connectedDevice).copy(isConnected = true)
                        )
                    )
                } else {
                    Action.ConnectionClosed(
                        BluetoothDeviceState.from(connectedDevice).copy(isConnected = false)
                    )
                }
            }
        }
    }

    private fun reduce(
        state: UiState,
        action: Action,
    ): UiState {
        return when (action) {
            /** Loading devices flow */
            is Action.LoadAvailableDevices -> state
            is Action.AvailableDevicesUpdated -> state.copy(foundDevices = action.devices)
            is Action.StopLoadingBluetoothDevices -> state

            /** Successful connection flow */
            is Action.ConnectBluetoothDevice -> state
            is Action.ConnectionInProgress -> state.copy(
                connectedDevice = action.device.copy(isLoading = true),
            )

            is Action.ConnectionSucceed -> state.copy(
                connectedDevice = action.device.copy(
                    isConnected = true,
                    isLoading = false,
                ),
            )

            /** Failure connection flow */
            is Action.ConnectionFailed -> state.copy(
                connectedDevice = action.device.copy(
                    isConnected = false,
                    isLoading = false,
                ),
                hasError = true,
            )

            is Action.NotifyErrorShown -> state.copy(
                hasError = false,
            )

            /** Closing connection flow */
            is Action.CloseConnection -> state.copy(
                connectedDevice = action.device.copy(isConnected = false),
            )

            is Action.ConnectionClosed -> state.copy(
                connectedDevice = action.device.copy(
                    isConnected = false,
                    isLoading = false,
                ),
            )
        }
    }
}