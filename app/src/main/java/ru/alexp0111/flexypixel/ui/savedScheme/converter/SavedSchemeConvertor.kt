package ru.alexp0111.flexypixel.ui.savedScheme.converter

import ru.alexp0111.flexypixel.business.saved_scheme.model.SavedSchemeBusinessModel
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiModel
import javax.inject.Inject

class SavedSchemeConvertor @Inject constructor() : ISavedSchemeConvertor {
    override fun convertToUiModel(dataModel: SavedSchemeBusinessModel): SavedSchemeUiModel =
        SavedSchemeUiModel(
            uuid = dataModel.uuid,
            title = dataModel.title ?: "No title",
            preview = dataModel.preview,
            createdAt = dataModel.createdAt ?: "Date undefined"
        )
}