package ru.alexp0111.flexypixel.ui.start.device_pairing

sealed interface Action {
    data object LoadAvailableDevices : Action
    data class AvailableDevicesUpdated(val devices: List<BluetoothDeviceState>) : Action
    data object StopLoadingBluetoothDevices : Action
    data class ConnectBluetoothDevice(val device: BluetoothDeviceState) : Action
    data class ConnectionInProgress(val device: BluetoothDeviceState) : Action
    data class ConnectionSucceed(val device: BluetoothDeviceState) : Action
    data class ConnectionFailed(val device: BluetoothDeviceState, val error: String) : Action
    data object NotifyErrorShown : Action
    data class CloseConnection(val device: BluetoothDeviceState) : Action
    data class ConnectionClosed(val device: BluetoothDeviceState) : Action
}

interface ActionConsumer {
    fun consumeAction(action: Action)
}