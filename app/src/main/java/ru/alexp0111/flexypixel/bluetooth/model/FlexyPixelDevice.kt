package ru.alexp0111.flexypixel.bluetooth.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import ru.alexp0111.flexypixel.ui.start.device_pairing.BluetoothDeviceState

@SuppressLint("MissingPermission")
data class FlexyPixelDevice(
    val name: String?,
    val address: String?,
) {
    companion object {
        fun from(device: BluetoothDevice): FlexyPixelDevice {
            return FlexyPixelDevice(
                name = device.name,
                address = device.address
            )
        }

        fun from(deviceState: BluetoothDeviceState): FlexyPixelDevice {
            return FlexyPixelDevice(
                name = deviceState.name,
                address = deviceState.address,
            )
        }
    }
}