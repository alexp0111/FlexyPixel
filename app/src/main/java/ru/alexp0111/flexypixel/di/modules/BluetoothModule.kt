package ru.alexp0111.flexypixel.di.modules

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {

    @Singleton
    @Provides
    fun provideBluetoothManager(context: Context): BluetoothManager {
        return context.getSystemService(BluetoothManager::class.java)
    }

    @Singleton
    @Provides
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter {
        return bluetoothManager.adapter
    }
}