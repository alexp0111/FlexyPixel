package ru.alexp0111.flexypixel.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.alexp0111.flexypixel.database.schemes.UserSavedSchemeDatabase
import ru.alexp0111.flexypixel.database.schemes.data.FrameCycleConverter
import ru.alexp0111.flexypixel.database.templates.PanelTemplateDatabase
import ru.alexp0111.flexypixel.database.templates.model.DrawingColorConverter
import ru.alexp0111.flexypixel.database.templates.model.PanelOrientationConverter
import javax.inject.Singleton

@Module
internal class DatabaseModule {
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

    @Provides
    @Singleton
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