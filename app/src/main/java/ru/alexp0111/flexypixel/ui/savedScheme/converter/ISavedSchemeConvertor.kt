package ru.alexp0111.flexypixel.ui.savedScheme.converter

import ru.alexp0111.flexypixel.business.saved_scheme.model.SavedSchemeBusinessModel
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiModel

interface ISavedSchemeConvertor {
    fun convertToUiModel(dataModel: SavedSchemeBusinessModel): SavedSchemeUiModel
}