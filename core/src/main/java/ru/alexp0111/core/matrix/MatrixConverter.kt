package ru.alexp0111.core.matrix

import ru.alexp0111.core.CommonSizeConstants

object MatrixConverter {
    fun indexToXY(index: Int, matrixSide: Int): Pair<Int, Int> {
        val x = index % matrixSide
        val y = index / matrixSide
        return Pair(x, y)
    }

    fun XYtoIndex(x: Int, y: Int, matrixSide: Int): Int {
        return y * matrixSide + x
    }

    fun localXtoGlobal(localX: Int, segmentNumber: Int): Int {
        val segmentX = segmentNumber % CommonSizeConstants.SEGMENTS_MATRIX_SIDE
        return segmentX * CommonSizeConstants.PANELS_MATRIX_SIDE + localX
    }

    fun localYtoGlobal(localY: Int, segmentNumber: Int): Int {
        val segmentY = segmentNumber / CommonSizeConstants.SEGMENTS_MATRIX_SIDE
        return segmentY * CommonSizeConstants.PANELS_MATRIX_SIDE + localY
    }
}