package ru.alexp0111.flexypixel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelOrientation

class PanelMetaDataTest {

    @Test
    fun `Check coordinates convert`() {
        assertEquals(Pair(0, 0), PanelMetaData.getAbsoluteCoordinates(0, 0))
        assertEquals(Pair(8, 0), PanelMetaData.getAbsoluteCoordinates(2, 2))
        assertEquals(Pair(0, 8), PanelMetaData.getAbsoluteCoordinates(6, 6))
        assertEquals(Pair(8, 8), PanelMetaData.getAbsoluteCoordinates(8, 8))

        assertEquals(Pair(5, 2), PanelMetaData.getAbsoluteCoordinates(8, 1))
        assertEquals(Pair(2, 5), PanelMetaData.getAbsoluteCoordinates(8, 3))

        assertEquals(Pair(6, 3), PanelMetaData.getAbsoluteCoordinates(0, 5))
        assertEquals(Pair(3, 6), PanelMetaData.getAbsoluteCoordinates(0, 7))
    }

    @Test
    fun `Check segment determination`() {
        val panel = PanelMetaData(
            order = 0,
            type = PanelMetaData.TYPE_64,
            absoluteX = 0,
            absoluteY = 0,
            orientation = PanelOrientation.LEFT,
            palette = PanelMetaData.getDefaultPalette(),
        )
        assertEquals(0, panel.copy(absoluteX = 0, absoluteY = 0).segment)
        assertEquals(2, panel.copy(absoluteX = 8, absoluteY = 0).segment)
        assertEquals(6, panel.copy(absoluteX = 0, absoluteY = 8).segment)
        assertEquals(8, panel.copy(absoluteX = 8, absoluteY = 8).segment)

        assertEquals(1, panel.copy(absoluteX = 5, absoluteY = 2).segment)
        assertEquals(3, panel.copy(absoluteX = 2, absoluteY = 5).segment)

        assertEquals(5, panel.copy(absoluteX = 6, absoluteY = 3).segment)
        assertEquals(7, panel.copy(absoluteX = 3, absoluteY = 6).segment)
    }
}