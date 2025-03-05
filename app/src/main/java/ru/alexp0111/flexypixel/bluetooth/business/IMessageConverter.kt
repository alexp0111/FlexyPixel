package ru.alexp0111.flexypixel.bluetooth.business

import ru.alexp0111.flexypixel.bluetooth.MessageFrame
import ru.alexp0111.flexypixel.bluetooth.MessageFramesMetaData
import ru.alexp0111.flexypixel.bluetooth.MessagePanelConfiguration
import ru.alexp0111.flexypixel.bluetooth.MessagePixel
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.PanelMetaData

internal interface IMessageConverter {
    fun convertPixelToMessage(panelOrder: Int, pixelOrder: Int, r: Int, g: Int, b: Int): MessagePixel
    fun convertFramesToMetaDataMessage(frames: List<MessageFrame>, interframeDelay: Int): MessageFramesMetaData
    fun convertFramesToMessage(frameCycle: FrameCycle): List<MessageFrame>
    fun convertPanelMetaDataToMessage(configuration: Set<PanelMetaData>): MessagePanelConfiguration
    fun parseIntToMessageString(num: Int, totalAmountOfSigns: Int = 3): String
}