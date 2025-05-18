package ru.alexp0111.bt_service.business

import kotlinx.coroutines.flow.SharedFlow
import ru.alexp0111.core.model.main.FrameCycle
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.core.model.main.PanelOrientation

interface IMessageHandler {
    fun sendPixel(panelOrder: Int, orientation: PanelOrientation, pixelOrder: Int, r: Int, g: Int, b: Int)
    fun sendFrames(frameCycle: FrameCycle)
    fun updateConfiguration(configuration: Set<PanelMetaData>)
    fun sendTestMessage(message: String)
    fun saveCurrentState()
    fun clearCurrentState()
    fun getErrorFlow(): SharedFlow<String>
    fun getResponseFlow(): SharedFlow<String>
}