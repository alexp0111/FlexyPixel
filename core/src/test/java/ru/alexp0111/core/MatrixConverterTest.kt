package ru.alexp0111.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.alexp0111.core.matrix.MatrixConverter
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatrixConverterTest {

    @ParameterizedTest
    @MethodSource("indexToXYSource")
    fun indexToXY(index: Int, expected: Pair<Int, Int>, matrixSide: Int) {
        assertEquals(expected, MatrixConverter.indexToXY(index, matrixSide))
    }

    @ParameterizedTest
    @MethodSource("XYtoIndexSource")
    fun XYtoIndex(x: Int, y: Int, expected: Int, matrixSide: Int) {
        assertEquals(expected, MatrixConverter.XYtoIndex(x, y, matrixSide))
    }

    @ParameterizedTest
    @MethodSource("localXtoGlobalSource")
    fun localXtoGlobal(x: Int, expected: Int, segmentNumber: Int) {
        assertEquals(expected, MatrixConverter.localXtoGlobal(x, segmentNumber))
    }

    @ParameterizedTest
    @MethodSource("localYtoGlobalSource")
    fun localYtoGlobal(y: Int, expected: Int, segmentNumber: Int) {
        assertEquals(expected, MatrixConverter.localYtoGlobal(y, segmentNumber))
    }

    private fun indexToXYSource(): Stream<Arguments> = Stream.of(
        Arguments.of(0, Pair(0, 0), 3),
        Arguments.of(1, Pair(1, 0), 3),
        Arguments.of(2, Pair(2, 0), 3),
        Arguments.of(3, Pair(0, 1), 3),
        Arguments.of(4, Pair(1, 1), 3),
        Arguments.of(5, Pair(2, 1), 3),
        Arguments.of(6, Pair(0, 2), 3),
        Arguments.of(7, Pair(1, 2), 3),
        Arguments.of(8, Pair(2, 2), 3),
    )

    private fun XYtoIndexSource(): Stream<Arguments> = Stream.of(
        Arguments.of(0, 0, 0, 3),
        Arguments.of(1, 0, 1, 3),
        Arguments.of(2, 0, 2, 3),
        Arguments.of(0, 1, 3, 3),
        Arguments.of(1, 1, 4, 3),
        Arguments.of(2, 1, 5, 3),
        Arguments.of(0, 2, 6, 3),
        Arguments.of(1, 2, 7, 3),
        Arguments.of(2, 2, 8, 3),
    )

    private fun localXtoGlobalSource(): Stream<Arguments> = Stream.of(
        Arguments.of(0, 0, 0),
        Arguments.of(1, 1, 0),
        Arguments.of(2, 2, 0),
        Arguments.of(0, 3, 1),
        Arguments.of(1, 4, 1),
        Arguments.of(2, 5, 1),
        Arguments.of(0, 6, 2),
        Arguments.of(1, 7, 2),
        Arguments.of(2, 8, 2),
        Arguments.of(0, 0, 3),
        Arguments.of(1, 1, 3),
        Arguments.of(2, 2, 3),
        Arguments.of(0, 3, 4),
        Arguments.of(1, 4, 4),
        Arguments.of(2, 5, 4),
        Arguments.of(0, 6, 5),
        Arguments.of(1, 7, 5),
        Arguments.of(2, 8, 5),
        Arguments.of(0, 0, 6),
        Arguments.of(1, 1, 6),
        Arguments.of(2, 2, 6),
        Arguments.of(0, 3, 7),
        Arguments.of(1, 4, 7),
        Arguments.of(2, 5, 7),
        Arguments.of(0, 6, 8),
        Arguments.of(1, 7, 8),
        Arguments.of(2, 8, 8),
    )

    private fun localYtoGlobalSource(): Stream<Arguments> = Stream.of(
        Arguments.of(0, 0, 0),
        Arguments.of(1, 1, 0),
        Arguments.of(2, 2, 0),
        Arguments.of(0, 0, 1),
        Arguments.of(1, 1, 1),
        Arguments.of(2, 2, 1),
        Arguments.of(0, 0, 2),
        Arguments.of(1, 1, 2),
        Arguments.of(2, 2, 2),
        Arguments.of(0, 3, 3),
        Arguments.of(1, 4, 3),
        Arguments.of(2, 5, 3),
        Arguments.of(0, 3, 4),
        Arguments.of(1, 4, 4),
        Arguments.of(2, 5, 4),
        Arguments.of(0, 3, 5),
        Arguments.of(1, 4, 5),
        Arguments.of(2, 5, 5),
        Arguments.of(0, 6, 6),
        Arguments.of(1, 7, 6),
        Arguments.of(2, 8, 6),
        Arguments.of(0, 6, 7),
        Arguments.of(1, 7, 7),
        Arguments.of(2, 8, 7),
        Arguments.of(0, 6, 8),
        Arguments.of(1, 7, 8),
        Arguments.of(2, 8, 8),
    )

}