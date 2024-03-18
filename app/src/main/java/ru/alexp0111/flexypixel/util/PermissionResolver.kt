package ru.alexp0111.flexypixel.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import javax.inject.Inject

class PermissionResolver @Inject constructor(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
) {

    fun isBluetoothAvailable(): Boolean {
        val permissionsGranted = isBluetoothPermissionsGranted()
        return permissionsGranted && bluetoothAdapter.isEnabled
    }

    fun isBluetoothPermissionsGranted(): Boolean {
        return granted(getNecessaryPermissions())
    }

    private fun granted(permissions: List<String>): Boolean {
        permissions.forEach {
            if (context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun getNecessaryPermissions(): List<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            }

            Build.VERSION.SDK_INT in (Build.VERSION_CODES.Q..<Build.VERSION_CODES.S) -> {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                )
            }

            else -> {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            }
        }
    }
}