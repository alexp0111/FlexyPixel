package ru.alexp0111.flexypixel.data.model

import ru.alexp0111.flexypixel.data.DrawingColor

data class PanelMetaData(
    var order: Int = 0,
    val type: Int = TYPE_64,
    var absoluteX: Int,
    var absoluteY: Int,
    var rotation: Int,
    var palette: MutableList<DrawingColor>
) {
    companion object {
        const val TYPE_64 = 64
        const val TYPE_256 = 256
        const val PALETTE_SIZE = 12
    }
}