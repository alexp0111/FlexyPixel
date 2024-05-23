package ru.alexp0111.flexypixel.data.model

import ru.alexp0111.flexypixel.bluetooth.MessageFrame
import java.lang.StringBuilder

data class Frame(
    var panels: MutableList<Panel>,
) {
    fun toMessageFrame(): MessageFrame {
        return MessageFrame(
            StringBuilder().apply {
                panels.forEach {
                    append(it.asMessage())
                }
            }.toString()
        )
    }
}