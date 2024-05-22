package ru.alexp0111.flexypixel

import org.junit.Assert
import org.junit.Test
import ru.alexp0111.flexypixel.data.model.PanelMetaData

class PanelMetaDataTest {

    @Test
    fun `Check coordinates convert`() {
        Assert.assertEquals(Pair(0, 0), PanelMetaData.getAbsoluteCoordinates(0, 0))
        Assert.assertEquals(Pair(8, 0), PanelMetaData.getAbsoluteCoordinates(2, 2))
        Assert.assertEquals(Pair(0, 8), PanelMetaData.getAbsoluteCoordinates(6, 6))
        Assert.assertEquals(Pair(8, 8), PanelMetaData.getAbsoluteCoordinates(8, 8))

        Assert.assertEquals(Pair(5, 2), PanelMetaData.getAbsoluteCoordinates(8, 1))
        Assert.assertEquals(Pair(2, 5), PanelMetaData.getAbsoluteCoordinates(8, 3))

        Assert.assertEquals(Pair(6, 3), PanelMetaData.getAbsoluteCoordinates(0, 5))
        Assert.assertEquals(Pair(3, 6), PanelMetaData.getAbsoluteCoordinates(0, 7))
    }

    @Test
    fun `Check segment determination`() {
        val panel = PanelMetaData(
            order = 0,
            type = PanelMetaData.TYPE_64,
            absoluteX = 0,
            absoluteY = 0,
            rotation = 0,
            palette = PanelMetaData.getDefaultPalette(),
        )
        Assert.assertEquals(0, panel.copy(absoluteX = 0, absoluteY = 0).segment)
        Assert.assertEquals(2, panel.copy(absoluteX = 8, absoluteY = 0).segment)
        Assert.assertEquals(6, panel.copy(absoluteX = 0, absoluteY = 8).segment)
        Assert.assertEquals(8, panel.copy(absoluteX = 8, absoluteY = 8).segment)

        Assert.assertEquals(1, panel.copy(absoluteX = 5, absoluteY = 2).segment)
        Assert.assertEquals(3, panel.copy(absoluteX = 2, absoluteY = 5).segment)

        Assert.assertEquals(5, panel.copy(absoluteX = 6, absoluteY = 3).segment)
        Assert.assertEquals(7, panel.copy(absoluteX = 3, absoluteY = 6).segment)
    }
}