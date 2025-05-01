package ru.alexp0111.flexypixel.database.templates.prepared

import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.PanelOrientation
import ru.alexp0111.flexypixel.database.templates.model.PanelTemplate

internal class PreparedTemplatesLoader {

    fun loadDefaultTemplates(): List<PanelTemplate> =
        listOf(
            PanelTemplate(pixels = getCat(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = getRusFlag(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = redTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = greenTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = blueTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = yellowTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = pinkTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = cyanTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = whiteTemplate(), orientations = getDefaultOrientations()),
            PanelTemplate(pixels = blackTemplate(), orientations = getDefaultOrientations()),
        )

    /**
     * Custom
     * */
    private fun getCat() = listOf(
        "975","975","533","533","533","533","975","975",
        "975","999","533","533","533","533","999","975",
        "533","975","975","975","975","975","975","533",
        "533","975","000","975","975","000","975","533",
        "533","975","000","975","975","000","975","533",
        "533","975","999","959","959","999","975","533",
        "999","975","999","999","999","999","975","999",
        "999","999","533","533","533","533","999","999",
    ).map { DrawingColor.getFromString(it) }

    private fun getRusFlag() = listOf(
        "999","999","999","999","999","999","999","999",
        "999","999","999","999","999","999","999","999",
        "999","999","999","999","999","999","999","999",
        "009","009","009","009","009","009","009","009",
        "009","009","009","009","009","009","009","009",
        "900","900","900","900","900","900","900","900",
        "900","900","900","900","900","900","900","900",
        "900","900","900","900","900","900","900","900",
    ).map { DrawingColor.getFromString(it) }

    /**
     * Colors
     * */
    private fun redTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("900") }
    private fun greenTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("090") }
    private fun blueTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("009") }

    private fun yellowTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("990") }
    private fun pinkTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("909") }
    private fun cyanTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("099") }

    private fun whiteTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("999") }
    private fun blackTemplate() = List(CommonSizeConstants.PANEL_PIXEL_TOTAL_AMOUNT) { DrawingColor.getFromString("000") }

    /**
     * Orientations
     * */
    private fun getDefaultOrientations(): List<PanelOrientation> =
        List(CommonSizeConstants.PANELS_TOTAL_AMOUNT) { PanelOrientation.LEFT }

}