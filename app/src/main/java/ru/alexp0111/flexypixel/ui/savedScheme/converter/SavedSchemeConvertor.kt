package ru.alexp0111.flexypixel.ui.savedScheme.converter

import androidx.core.net.toUri
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiModel
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import javax.inject.Inject

class SavedSchemeConvertor @Inject constructor() : ISavedSchemeConvertor {
    override fun convertToUiModel(dataModel: UserSavedScheme): SavedSchemeUiModel =
        SavedSchemeUiModel(
            uuid = dataModel.id,
            title = dataModel.title,
            preview = dataModel.preview?.toUri(),
            createdAt = dataModel.createdAt
        )
}