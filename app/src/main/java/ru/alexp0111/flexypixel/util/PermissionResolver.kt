package ru.alexp0111.flexypixel.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import javax.inject.Inject

class PermissionResolver @Inject constructor(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
    private val locationManager: LocationManager,
) {

    fun isSystemCompletelyReady(): Boolean {
        return isBluetoothPermissionsGranted() && isBluetoothOn() && isGPSOn()
    }

    fun isBluetoothOn(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun isGPSOn(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isBluetoothPermissionsGranted(): Boolean {
        return granted(getNecessaryPermissions())
    }

    private fun granted(permissions: Array<String>): Boolean {
        permissions.forEach {
            if (context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun getNecessaryPermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            }

            else -> {
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            }
        }
    }
}