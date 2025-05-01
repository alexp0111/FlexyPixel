package ru.alexp0111.local_repo.database.templates.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.PanelOrientation

@Entity(tableName = "panel_templates")
data class PanelTemplate(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val pixels: List<DrawingColor>,
    val orientations: List<PanelOrientation>,
)