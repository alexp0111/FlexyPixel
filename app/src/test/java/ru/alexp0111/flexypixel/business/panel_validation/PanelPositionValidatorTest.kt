package ru.alexp0111.flexypixel.business.panel_validation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelOrientation
import ru.alexp0111.flexypixel.data.model.PanelStatus
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PanelPositionValidatorTest {

    private val validator = PanelPositionValidator()

    @ParameterizedTest
    @MethodSource("panelSource")
    fun validate(
        incomingPanels: Set<PanelMetaData>,
        expectedPanels: Set<PanelMetaData>,
    ) {
        assertEquals(expectedPanels, validator.validate(incomingPanels))
    }

    private fun panelSource(): Stream<Arguments> = Stream.of(
        /*
        [ ] [0] [1] |
        [ ] [ ] [ ] |
        [ ] [ ] [ ] |
        -------------
         */
        Arguments.of(
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 1,
                    absoluteY = 0,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 2,
                    absoluteY = 0,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 1,
                    absoluteY = 0,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 2,
                    absoluteY = 0,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
            ),
        ),

        /*
        [ ] [ ] [ ] | [ ] [ ] [ ]
        [ ] [ ] [0] | [1] [ ] [ ]
        [ ] [ ] [ ] | [ ] [ ] [ ]
        -------------------------
         */
        Arguments.of(
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 3,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 3,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
            ),
        ),

        /*
        [ ] [ ] [ ] | [ ] [ ] [ ]
        [ ] [ ] [0] | [ ] [1] [ ]
        [ ] [ ] [ ] | [ ] [ ] [ ]
        -------------------------
         */
        Arguments.of(
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 4,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 4,
                    absoluteY = 1,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
        ),

        /*
        [ ] [ ] [ ] | [ ] [ ] [ ]
        [ ] [ ] [0] | [ ] [1] [2]
        [ ] [ ] [ ] | [ ] [ ] [ ]
        -------------------------
         */
        Arguments.of(
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 4,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 2,
                    absoluteX = 5,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
            ),
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.SELECTED,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 4,
                    absoluteY = 1,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 2,
                    absoluteX = 5,
                    absoluteY = 1,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
        ),

        /*
        [ ] [ ] [ ] | [ ] [ ] [ ]
        [ ] [0] [1] | [ ] [ ] [ ]
        [ ] [ ] [2] | [3] [ ] [ ]
        -------------------------
        [ ] [8] [ ] | [4] [ ] [ ]
        [ ] [7] [6] | [5] [ ] [ ]
        [ ] [ ] [ ] | [ ] [ ] [ ]
         */
        Arguments.of(
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 1,
                    absoluteY = 1,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 2,
                    absoluteX = 2,
                    absoluteY = 2,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 3,
                    absoluteX = 3,
                    absoluteY = 2,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 4,
                    absoluteX = 3,
                    absoluteY = 3,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 5,
                    absoluteX = 3,
                    absoluteY = 4,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 6,
                    absoluteX = 2,
                    absoluteY = 4,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 7,
                    absoluteX = 1,
                    absoluteY = 4,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 8,
                    absoluteX = 1,
                    absoluteY = 3,
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.UP,
                ),
            ),
            setOf(
                PanelMetaData(
                    order = 0,
                    absoluteX = 1,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 1,
                    absoluteX = 2,
                    absoluteY = 1,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.DOWN,
                ),
                PanelMetaData(
                    order = 2,
                    absoluteX = 2,
                    absoluteY = 2,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.RIGHT,
                ),
                PanelMetaData(
                    order = 3,
                    absoluteX = 3,
                    absoluteY = 2,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.DOWN,
                ),
                PanelMetaData(
                    order = 4,
                    absoluteX = 3,
                    absoluteY = 3,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.DOWN,
                ),
                PanelMetaData(
                    order = 5,
                    absoluteX = 3,
                    absoluteY = 4,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 6,
                    absoluteX = 2,
                    absoluteY = 4,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
                PanelMetaData(
                    order = 7,
                    absoluteX = 1,
                    absoluteY = 4,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.UP,
                ),
                PanelMetaData(
                    order = 8,
                    absoluteX = 1,
                    absoluteY = 3,
                    status = PanelStatus.DEFAULT,
                    orientation = PanelOrientation.LEFT,
                ),
            ),
        ),
    )
}