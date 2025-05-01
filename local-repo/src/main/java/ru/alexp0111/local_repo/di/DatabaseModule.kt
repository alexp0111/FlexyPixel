package ru.alexp0111.local_repo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.alexp0111.core.di.AppScope
import ru.alexp0111.local_repo.database.schemes.UserSavedSchemeDatabase
import ru.alexp0111.local_repo.database.schemes.data.FrameCycleConverter
import ru.alexp0111.local_repo.database.templates.PanelTemplateDatabase
import ru.alexp0111.local_repo.database.templates.model.DrawingColorConverter
import ru.alexp0111.local_repo.database.templates.model.PanelOrientationConverter

@Module
class DatabaseModule {
    @Provides
    @AppScope
    fun provideUserSavedSchemeDatabase(context: Context): UserSavedSchemeDatabase {
        return Room.databaseBuilder(
            context,
            UserSavedSchemeDatabase::class.java,
            "user_saved_scheme_database",
        ).addTypeConverter(FrameCycleConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @AppScope
    fun providePanelTemplateDatabase(context: Context): PanelTemplateDatabase {
        return Room.databaseBuilder(
            context,
            PanelTemplateDatabase::class.java,
            "panel_template_database",
        ).addTypeConverter(DrawingColorConverter())
            .addTypeConverter(PanelOrientationConverter())
            .fallbackToDestructiveMigration()
            .build()
    }
}