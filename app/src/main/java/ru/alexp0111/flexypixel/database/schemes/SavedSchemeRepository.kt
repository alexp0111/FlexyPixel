package ru.alexp0111.flexypixel.database.schemes

import kotlinx.coroutines.flow.Flow
import ru.alexp0111.flexypixel.database.schemes.data.UserSavedScheme
import javax.inject.Inject

class SavedSchemeRepository @Inject constructor(
    private val userSavedSchemeDatabase: UserSavedSchemeDatabase,
) {
    private val userSavedSchemeDao = userSavedSchemeDatabase.userSavedSchemeDao()

    suspend fun insertNewScheme(userSavedScheme: UserSavedScheme): Long {
        return userSavedSchemeDao.insertNewScheme(userSavedScheme)
    }

    suspend fun updateExistingScheme(userSavedScheme: UserSavedScheme) {
        userSavedSchemeDao.updateExistingScheme(userSavedScheme)
    }

    suspend fun deleteExistingScheme(userSavedScheme: UserSavedScheme) {
        userSavedSchemeDao.deleteExistingScheme(userSavedScheme)
    }

    fun getAllSchemes(): Flow<List<UserSavedScheme>> {
        return userSavedSchemeDao.getAllSchemes()
    }

    suspend fun getSchemeById(schemeId: Int): UserSavedScheme {
        return userSavedSchemeDao.getSchemeById(schemeId)
    }
}