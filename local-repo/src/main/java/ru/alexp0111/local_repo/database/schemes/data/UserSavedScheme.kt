package ru.alexp0111.local_repo.database.schemes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexp0111.core.model.main.FrameCycle
import java.util.UUID

@Entity(tableName = "user_saved_schemes")
data class UserSavedScheme(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var createdAt: String,
    var preview: String?,
    var frameCycle: FrameCycle,
)