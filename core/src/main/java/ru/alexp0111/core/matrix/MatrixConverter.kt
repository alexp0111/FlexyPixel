package ru.alexp0111.core.matrix

object MatrixConverter {
    fun indexToXY(index: Int, matrixSide: Int): Pair<Int, Int> {
        val x = index % matrixSide
        val y = index / matrixSide
        return Pair(x, y)
    }

    fun XYtoIndex(x: Int, y: Int, matrixSide: Int): Int {
        return y * matrixSide + x
    }
}