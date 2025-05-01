package ru.alexp0111.bt_service.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

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
    }
}