package ru.alexp0111.flexypixel.business.saved_scheme

import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme

interface ISavedSchemeInteractor {
    suspend fun getAllSchemes(): List<UserSavedScheme>
}