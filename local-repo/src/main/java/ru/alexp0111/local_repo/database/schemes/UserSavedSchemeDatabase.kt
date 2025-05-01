package ru.alexp0111.local_repo.database.schemes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.alexp0111.local_repo.database.schemes.data.FrameCycleConverter
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme

@Database(entities = [UserSavedScheme::class], version = 1)
@TypeConverters(FrameCycleConverter::class)
abstract class UserSavedSchemeDatabase : RoomDatabase() {
    abstract fun userSavedSchemeDao(): UserSavedSchemeDao
}