package ru.alexp0111.flexypixel.data.model

import ru.alexp0111.flexypixel.bluetooth.MessagePanelConfiguration
import ru.alexp0111.flexypixel.bluetooth.utils.MessageConverter

data class PanelConfiguration(
    var listOfMetaData: MutableList<PanelMetaData>,
) {

    fun getPanelMetaDataByCoordinates(x: Int, y: Int): PanelMetaData? {
        return listOfMetaData.find { it.absoluteX == x && it.absoluteY == y }
    }

    fun asMessage(): MessagePanelConfiguration {
        val arrayOfTypes = Array(MAX_SIZE) { "000" }
        listOfMetaData.forEachIndexed { index, panelMetaData ->
            arrayOfTypes[index] = MessageConverter.convert(panelMetaData.type)
        }
        return MessagePanelConfiguration(arrayOfTypes.joinToString(""))
    }

    companion object {
        const val MAX_SIZE = 9
    }
}