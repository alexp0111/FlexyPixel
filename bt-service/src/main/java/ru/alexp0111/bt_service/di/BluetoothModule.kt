package ru.alexp0111.bt_service.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.alexp0111.bt_service.business.IMessageHandler
import ru.alexp0111.bt_service.business.MessageHandler
import ru.alexp0111.bt_service.model.message.converter.IMessageConverter
import ru.alexp0111.bt_service.model.message.converter.MessageConverter
import ru.alexp0111.core.di.AppScope

@Module
class BluetoothModule {

    @AppScope
    @Provides
    fun provideBluetoothManager(context: Context): BluetoothManager {
        return context.getSystemService(BluetoothManager::class.java)
    }

    @AppScope
    @Provides
    fun provideLocationManager(context: Context): LocationManager {
        return context.getSystemService(LocationManager::class.java)
    }

    @AppScope
    @Provides
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter {
        return bluetoothManager.adapter
    }
}

@Module
interface BluetoothBindsModule {
    @AppScope
    @Binds
    fun bindMessageConverter(messageConverter: MessageConverter): IMessageConverter

    @AppScope
    @Binds
    fun bindMessageHandler(messageHandler: MessageHandler): IMessageHandler
}