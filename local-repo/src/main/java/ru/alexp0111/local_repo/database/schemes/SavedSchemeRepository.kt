package ru.alexp0111.local_repo.database.schemes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import javax.inject.Inject

class SavedSchemeRepository @Inject constructor(
    userSavedSchemeDatabase: UserSavedSchemeDatabase,
) {
    private val userSavedSchemeDao by lazy {
        userSavedSchemeDatabase.userSavedSchemeDao()
    }

    suspend fun insertNewScheme(userSavedScheme: UserSavedScheme): Long = withContext(Dispatchers.IO) {
        userSavedSchemeDao.insertNewScheme(userSavedScheme)
    }

    suspend fun updateExistingScheme(userSavedScheme: UserSavedScheme) = withContext(Dispatchers.IO) {
        userSavedSchemeDao.updateExistingScheme(userSavedScheme)
    }

    suspend fun deleteExistingScheme(userSavedScheme: UserSavedScheme) = withContext(Dispatchers.IO) {
        userSavedSchemeDao.deleteExistingScheme(userSavedScheme)
    }

    suspend fun getAllSchemes(): List<UserSavedScheme> = withContext(Dispatchers.IO) {
        userSavedSchemeDao.getAllSchemes()
    }

    suspend fun getSchemeById(schemeId: String): UserSavedScheme = withContext(Dispatchers.IO) {
        userSavedSchemeDao.getSchemeById(schemeId)
    }
}