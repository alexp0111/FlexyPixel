package ru.alexp0111.bt_service.business

import kotlinx.coroutines.flow.SharedFlow
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.PanelMetaData

interface IMessageHandler {
    fun sendPixel(panelOrder: Int, pixelOrder: Int, r: Int, g: Int, b: Int)
    fun sendFrames(frameCycle: FrameCycle)
    fun updateConfiguration(configuration: Set<PanelMetaData>)
    fun sendTestMessage(message: String)
    fun saveCurrentState()
    fun clearCurrentState()
    fun getErrorFlow(): SharedFlow<String>
    fun getResponseFlow(): SharedFlow<String>
}