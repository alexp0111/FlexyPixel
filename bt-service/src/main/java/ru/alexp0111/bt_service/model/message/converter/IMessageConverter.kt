package ru.alexp0111.bt_service.model.message.converter

import ru.alexp0111.bt_service.model.message.MessageFrame
import ru.alexp0111.bt_service.model.message.MessageFramesMetaData
import ru.alexp0111.bt_service.model.message.MessagePanelConfiguration
import ru.alexp0111.bt_service.model.message.MessagePixel
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.PanelMetaData

interface IMessageConverter {
    fun convertPixelToMessage(panelOrder: Int, pixelOrder: Int, r: Int, g: Int, b: Int): MessagePixel
    fun convertFramesToMetaDataMessage(frames: List<MessageFrame>, interframeDelay: Int): MessageFramesMetaData
    fun convertFramesToMessage(frameCycle: FrameCycle): List<MessageFrame>
    fun convertPanelMetaDataToMessage(configuration: Set<PanelMetaData>): MessagePanelConfiguration
    fun parseIntToMessageString(num: Int, totalAmountOfSigns: Int = 3): String
}