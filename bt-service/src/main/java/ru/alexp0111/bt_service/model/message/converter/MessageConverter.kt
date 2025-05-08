package ru.alexp0111.bt_service.model.message.converter

import ru.alexp0111.bt_service.model.message.MessageFrame
import ru.alexp0111.bt_service.model.message.MessageFramesMetaData
import ru.alexp0111.bt_service.model.message.MessagePanelConfiguration
import ru.alexp0111.bt_service.model.message.MessagePixel
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.Coordinate
import ru.alexp0111.core.model.Frame
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.Panel
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.core.model.PanelOrientation
import javax.inject.Inject

class MessageConverter @Inject constructor() : IMessageConverter {

    override fun convertPixelToMessage(
        panelOrder: Int,
        orientation: PanelOrientation,
        pixelOrder: Int,
        r: Int,
        g: Int,
        b: Int
    ): MessagePixel {
        // get coordinates
        val coordinate = Coordinate(
            x = pixelOrder % CommonSizeConstants.PANEL_PIXEL_SIDE,
            y = pixelOrder / CommonSizeConstants.PANEL_PIXEL_SIDE
        )
        // rotate left
        val rotatedLeftCoordinate = Coordinate(
            x = coordinate.y,
            y = CommonSizeConstants.PANEL_PIXEL_SIDE - coordinate.x - 1
        )
        // mirror x
        var mirroredCoordinate = Coordinate(
            x = CommonSizeConstants.PANEL_PIXEL_SIDE - rotatedLeftCoordinate.x - 1,
            y = rotatedLeftCoordinate.y,
        )
        // rotate with orientation
        repeat(getRotationTimesByOrientation(orientation)) {
            mirroredCoordinate = Coordinate(
                x = CommonSizeConstants.PANEL_PIXEL_SIDE - mirroredCoordinate.y - 1,
                y = mirroredCoordinate.x
            )
        }
        // convert to list position
        val trimmedOrder = mirroredCoordinate.y * CommonSizeConstants.PANEL_PIXEL_SIDE + mirroredCoordinate.x

        return MessagePixel(
            panelPosition = panelOrder,
            pixelPosition = parseIntToMessageString(trimmedOrder),
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
        val preRotated = rotateMatrixByClock(panel.pixels, 3)
        val mirrored = mirrorMatrixByVertical(preRotated)
        val rotationTimes = getRotationTimesByOrientation(orientation)
        val pixels = rotateMatrixByClock(mirrored, rotationTimes)
        return Panel(pixels)
    }

    private fun getRotationTimesByOrientation(orientation: PanelOrientation) =
        when (orientation) {
            PanelOrientation.LEFT -> 0
            PanelOrientation.DOWN -> 3
            PanelOrientation.RIGHT -> 2
            PanelOrientation.UP -> 1
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

    private fun mirrorMatrixByVertical(pixels: Array<String>): Array<String> {
        val mirroredMatrix = mutableListOf<String>()
        pixels.toList().windowed(CommonSizeConstants.PANEL_PIXEL_SIDE, CommonSizeConstants.PANEL_PIXEL_SIDE) {
            mirroredMatrix.addAll(it.reversed())
        }
        return mirroredMatrix.toTypedArray()
    }

}

internal fun Frame.toMessageFrame(): MessageFrame =
    MessageFrame(
        buildString {
            panels.forEach { append(it.pixels.joinToString("")) }
        }
    )