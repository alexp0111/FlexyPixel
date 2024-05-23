package ru.alexp0111.flexypixel.database.schemes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.alexp0111.flexypixel.database.schemes.data.UserSavedScheme

@Dao
interface UserSavedSchemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewScheme(userSavedScheme: UserSavedScheme): Long

    @Update
    suspend fun updateExistingScheme(userSavedScheme: UserSavedScheme)

    @Delete
    suspend fun deleteExistingScheme(userSavedScheme: UserSavedScheme)

    @Query("SELECT * FROM user_saved_schemes")
    fun getAllSchemes(): Flow<List<UserSavedScheme>>

    @Query("SELECT * FROM user_saved_schemes WHERE id=:schemeId")
    suspend fun getSchemeById(schemeId: Int): UserSavedScheme
}