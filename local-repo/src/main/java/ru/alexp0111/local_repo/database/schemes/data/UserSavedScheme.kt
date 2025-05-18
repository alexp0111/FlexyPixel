package ru.alexp0111.local_repo.database.schemes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexp0111.core.model.main.FrameCycle

@Entity(tableName = "user_saved_schemes")
data class UserSavedScheme(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var frameCycle: FrameCycle,
)