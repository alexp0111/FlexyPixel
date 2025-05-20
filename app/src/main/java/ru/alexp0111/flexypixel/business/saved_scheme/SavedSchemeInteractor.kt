package ru.alexp0111.flexypixel.business.saved_scheme

import ru.alexp0111.local_repo.database.schemes.SavedSchemeRepository
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import javax.inject.Inject

class SavedSchemeInteractor @Inject constructor(
    private val databaseRepository: SavedSchemeRepository,
) : ISavedSchemeInteractor {
    override suspend fun getAllSchemes(): List<UserSavedScheme> {
        return databaseRepository.getAllSchemes()
    }
}