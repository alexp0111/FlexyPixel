package ru.alexp0111.flexypixel.database.schemes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexp0111.flexypixel.data.model.FrameCycle

@Entity(tableName = "user_saved_schemes")
data class UserSavedScheme(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var frameCycle: FrameCycle,
)