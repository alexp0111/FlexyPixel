package ru.alexp0111.flexypixel.business.panel_validation

import ru.alexp0111.flexypixel.data.model.PanelMetaData

internal interface IPanelPositionValidator {
    fun checkIfSelectedAreaSquare(panels: Set<PanelMetaData>): Boolean
    fun validate(panels: Set<PanelMetaData>): Set<PanelMetaData>
}