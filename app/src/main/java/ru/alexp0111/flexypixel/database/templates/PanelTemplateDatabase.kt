package ru.alexp0111.flexypixel.database.templates

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.alexp0111.flexypixel.database.templates.model.PanelTemplate
import ru.alexp0111.flexypixel.database.templates.model.DrawingColorConverter
import ru.alexp0111.flexypixel.database.templates.model.PanelOrientationConverter

@Database(entities = [PanelTemplate::class], version = 1)
@TypeConverters(DrawingColorConverter::class, PanelOrientationConverter::class)
internal abstract class PanelTemplateDatabase : RoomDatabase() {
    abstract fun panelTemplateDao(): PanelTemplateDao
}