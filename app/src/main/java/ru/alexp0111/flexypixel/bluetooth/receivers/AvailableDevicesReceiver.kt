package ru.alexp0111.flexypixel.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.alexp0111.flexypixel.ui.util.getBluetoothDevice

class AvailableDevicesReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device = intent.getBluetoothDevice() ?: return
                onDeviceFound(device)
            }
        }
    }
}