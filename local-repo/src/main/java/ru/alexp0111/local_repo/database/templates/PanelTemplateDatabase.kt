package ru.alexp0111.local_repo.database.templates

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.alexp0111.local_repo.database.templates.model.DrawingColorConverter
import ru.alexp0111.local_repo.database.templates.model.PanelOrientationConverter
import ru.alexp0111.local_repo.database.templates.model.PanelTemplate

@Database(entities = [PanelTemplate::class], version = 1)
@TypeConverters(DrawingColorConverter::class, PanelOrientationConverter::class)
abstract class PanelTemplateDatabase : RoomDatabase() {
    abstract fun panelTemplateDao(): PanelTemplateDao
}