package ru.alexp0111.local_repo.database.templates.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.PanelOrientation

private const val SEPARATOR = ","

@ProvidedTypeConverter
internal class DrawingColorConverter {
    @TypeConverter
    fun fromPixels(pixels: List<DrawingColor>): String {
        return pixels.joinToString(SEPARATOR) { it.asString() }
    }

    @TypeConverter
    fun toPixels(pixels: String): List<DrawingColor> {
        return pixels.split(SEPARATOR).map { DrawingColor.getFromString(it) }
    }
}

@ProvidedTypeConverter
internal class PanelOrientationConverter {
    @TypeConverter
    fun fromOrientations(pixels: List<PanelOrientation>): String {
        return pixels.joinToString(SEPARATOR) { it.name }
    }

    @TypeConverter
    fun toOrientations(pixels: String): List<PanelOrientation> {
        return pixels.split(SEPARATOR).map { PanelOrientation.valueOf(it) }
    }
}