package ru.alexp0111.flexypixel.ui.start.device_pairing

import kotlinx.coroutines.flow.StateFlow
import ru.alexp0111.flexypixel.bluetooth.model.FlexyPixelDevice

data class UiState(
    val hasError: Boolean = false,
    val foundDevices: List<BluetoothDeviceState> = emptyList(),
    val connectedDevice: BluetoothDeviceState? = null,
)

data class BluetoothDeviceState(
    val name: String = "",
    val address: String = "",
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
) {

    companion object {
        fun from(flexyPixelDevice: FlexyPixelDevice): BluetoothDeviceState {
            return BluetoothDeviceState(
                name = flexyPixelDevice.name ?: "",
                address = flexyPixelDevice.address ?: "",
                isConnected = false,
                isLoading = false,
            )
        }
    }
}

interface StateHolder {
    val state: StateFlow<UiState>
}