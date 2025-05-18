package ru.alexp0111.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.alexp0111.core.model.Coordinate
import ru.alexp0111.core.model.PanelMetaData
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PanelMetaDataTest {

    private val panel = PanelMetaData(
        absoluteX = 0,
        absoluteY = 0,
    )

    @ParameterizedTest
    @MethodSource("localToAbsoluteCoordinates")
    fun `Check coordinates convert`(absCoordinate: Coordinate, localIndex: Int, segment: Int) {
        assertEquals(absCoordinate, PanelMetaData.getAbsoluteCoordinates(localIndex, segment))
    }

    @ParameterizedTest
    @MethodSource("determineSegment")
    fun `Check segment determination`(expectedSegment: Int, coordinate: Coordinate) {
        assertEquals(expectedSegment, panel.copy(absoluteX = coordinate.x, absoluteY = coordinate.y).segment)
    }

    private fun localToAbsoluteCoordinates(): Stream<Arguments> = Stream.of(
        Arguments.of(Coordinate(0, 0), 0, 0),
        Arguments.of(Coordinate(8, 0), 2, 2),
        Arguments.of(Coordinate(0, 8), 6, 6),
        Arguments.of(Coordinate(8, 8), 8, 8),
        Arguments.of(Coordinate(5, 2), 8, 1),
        Arguments.of(Coordinate(2, 5), 8, 3),
        Arguments.of(Coordinate(6, 3), 0, 5),
        Arguments.of(Coordinate(3, 6), 0, 7),
    )

    private fun determineSegment(): Stream<Arguments> = Stream.of(
        Arguments.of(2, Coordinate(8, 0)),
        Arguments.of(6, Coordinate(0, 8)),
        Arguments.of(8, Coordinate(8, 8)),
        Arguments.of(1, Coordinate(5, 2)),
        Arguments.of(3, Coordinate(2, 5)),
        Arguments.of(5, Coordinate(6, 3)),
        Arguments.of(7, Coordinate(3, 6)),
    )
}