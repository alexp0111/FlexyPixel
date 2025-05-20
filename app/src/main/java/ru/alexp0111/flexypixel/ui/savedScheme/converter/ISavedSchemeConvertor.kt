package ru.alexp0111.flexypixel.ui.savedScheme.converter

import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiModel
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme

interface ISavedSchemeConvertor {
    fun convertToUiModel(dataModel: UserSavedScheme): SavedSchemeUiModel
}