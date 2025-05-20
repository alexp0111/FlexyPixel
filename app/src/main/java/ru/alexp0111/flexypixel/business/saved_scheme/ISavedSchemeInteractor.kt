package ru.alexp0111.flexypixel.business.saved_scheme

import ru.alexp0111.flexypixel.business.saved_scheme.model.SavedSchemeBusinessModel

interface ISavedSchemeInteractor {
    suspend fun getAllSchemes(): List<SavedSchemeBusinessModel>
}