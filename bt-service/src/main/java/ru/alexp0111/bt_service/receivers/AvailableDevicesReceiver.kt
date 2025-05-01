package ru.alexp0111.bt_service.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.alexp0111.bt_service.utils.getBluetoothDevice

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