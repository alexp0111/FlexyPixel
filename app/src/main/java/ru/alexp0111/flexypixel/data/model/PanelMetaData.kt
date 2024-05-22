package ru.alexp0111.flexypixel.data.model

import ru.alexp0111.flexypixel.data.DrawingColor

data class PanelMetaData(
    var order: Int = 0,
    val type: Int = TYPE_64,
    var absoluteX: Int,
    var absoluteY: Int,
    var rotation: Int,
    var palette: MutableList<DrawingColor>,
) {

    val segment: Int
        get() {
            return (absoluteY / 3) * 3 + (absoluteX / 3)
        }

    companion object {
        const val TYPE_64 = 64
        const val TYPE_256 = 256
        const val PALETTE_SIZE = 12

        fun getAbsoluteCoordinates(
            indexIn3By3MatrixRepresentedAsList: Int,
            segmentNum: Int,
        ): Pair<Int, Int> {
            val xOffset = segmentNum % 3 * 3
            val yOffset = (segmentNum / 3) * 3
            val x = indexIn3By3MatrixRepresentedAsList % 3 + xOffset
            val y = indexIn3By3MatrixRepresentedAsList / 3 + yOffset
            return Pair(x, y)
        }

        fun getDefaultPalette(): MutableList<DrawingColor> {
            return MutableList(PALETTE_SIZE) { DrawingColor(0, 0, 0) }
        }
    }
}