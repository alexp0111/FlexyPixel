package ru.alexp0111.core.matrix

object MatrixConverter {
    fun indexToXY(index: Int, matrixSide: Int): Pair<Int, Int> {
        val x = index % matrixSide
        val y = index / matrixSide
        return Pair(x, y)
    }
}