package ru.alexp0111.flexypixel.bluetooth.business

import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.Frame
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.Panel
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.core.model.PanelOrientation
import ru.alexp0111.flexypixel.bluetooth.MessageFrame
import ru.alexp0111.flexypixel.bluetooth.MessageFramesMetaData
import ru.alexp0111.flexypixel.bluetooth.MessagePanelConfiguration
import ru.alexp0111.flexypixel.bluetooth.MessagePixel
import javax.inject.Inject

internal class MessageConverter @Inject constructor() : IMessageConverter {

    override fun convertPixelToMessage(panelOrder: Int, pixelOrder: Int, r: Int, g: Int, b: Int): MessagePixel {
        val reversedPixelPosition = CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT - pixelOrder - 1
        return MessagePixel(
            panelPosition = panelOrder,
            pixelPosition = parseIntToMessageString(reversedPixelPosition),
            pixelColor = "$r$g$b"
        )
    }

    override fun convertFramesToMetaDataMessage(
        frames: List<MessageFrame>,
        interframeDelay: Int,
    ): MessageFramesMetaData = MessageFramesMetaData(
        parseIntToMessageString(frames.size),
        parseIntToMessageString(interframeDelay, MessageFramesMetaData.INTERFRAME_SIZE),
    )

    override fun convertFramesToMessage(frameCycle: FrameCycle): List<MessageFrame> =
        frameCycle.frames.map { frame ->
            val rotatedPanels = frame.panels.mapIndexed { order, panel ->
                val panelMetaData = frameCycle.configuration.listOfMetaData.find { it.order == order }
                if (panelMetaData != null) applyRotationToPanel(panel, panelMetaData.orientation) else panel
            }
            frame.copy(panels = rotatedPanels.toMutableList()).toMessageFrame()
        }.toList()

    override fun convertPanelMetaDataToMessage(configuration: Set<PanelMetaData>): MessagePanelConfiguration {
        val stringConfiguration = StringBuilder().apply {
            configuration.sortedBy { it.order }.forEach {
                append(parseIntToMessageString(it.type, 3))
            }
            while (this.length != CommonSizeConstants.PANELS_TOTAL_AMOUNT * 3) {
                append("000")
            }
        }

        return MessagePanelConfiguration(stringConfiguration.toString())
    }

    override fun parseIntToMessageString(num: Int, totalAmountOfSigns: Int): String {
        val stringNumber = num.toString()
        return when {
            stringNumber.length == totalAmountOfSigns -> stringNumber
            stringNumber.length > totalAmountOfSigns -> throw IllegalStateException("Integer value is too big")
            else -> "0".repeat(totalAmountOfSigns - stringNumber.length) + stringNumber
        }
    }

    private fun applyRotationToPanel(panel: Panel, orientation: PanelOrientation): Panel {
        val rotationTimes = when (orientation) {
            PanelOrientation.LEFT -> 0
            PanelOrientation.DOWN -> 1
            PanelOrientation.RIGHT -> 2
            PanelOrientation.UP -> 3
        }

        val pixels = rotateMatrixByClock(panel.pixels, rotationTimes)
            .reversedArray() // Specialty of panel hardware organization

        return Panel(pixels)
    }

    private fun rotateMatrixByClock(pixels: Array<String>, times: Int): Array<String> {
        if (times == 0) return pixels
        val matrixSide = CommonSizeConstants.PANEL_PIXEL_SIDE
        val rotatedMatrix = mutableListOf<String>()

        for (newRow in 0 until matrixSide) {
            for (oldColumn in matrixSide - 1 downTo 0) {
                rotatedMatrix.add(pixels[oldColumn * matrixSide + newRow])
            }
        }

        return rotateMatrixByClock(rotatedMatrix.toTypedArray(), times - 1)
    }

}

internal fun Frame.toMessageFrame(): MessageFrame =
    MessageFrame(
        buildString {
            panels.forEach { append(it::asMessage) }
        }
    )