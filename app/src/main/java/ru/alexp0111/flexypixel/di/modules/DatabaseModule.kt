package ru.alexp0111.flexypixel.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.alexp0111.flexypixel.database.schemes.UserSavedSchemeDatabase
import ru.alexp0111.flexypixel.database.schemes.data.FrameCycleConverter
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideUserSavedSchemeDatabase(context: Context): UserSavedSchemeDatabase {
        return Room.databaseBuilder(
            context,
            UserSavedSchemeDatabase::class.java,
            "user_saved_scheme_database",
        ).addTypeConverter(FrameCycleConverter())
            .fallbackToDestructiveMigration()
            .build()
    }
}