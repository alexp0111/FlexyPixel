package ru.alexp0111.flexypixel.ui.util

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build

fun Intent.getBluetoothDevice(): BluetoothDevice? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        }

        else -> {
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
    }
}