package ru.alexp0111.flexypixel.database.schemes.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.alexp0111.flexypixel.data.model.FrameCycle

@ProvidedTypeConverter
class FrameCycleConverter {
    @TypeConverter
    fun fromFrameCycle(frameCycle: FrameCycle): String {
        return Gson().toJson(frameCycle)
    }

    @TypeConverter
    fun toFrameCycle(data: String): FrameCycle {
        return Gson().fromJson(data, FrameCycle::class.java)
    }
}