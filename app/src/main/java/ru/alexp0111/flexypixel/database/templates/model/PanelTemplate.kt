package ru.alexp0111.flexypixel.database.templates.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexp0111.core_ui.common.model.DrawingColor
import ru.alexp0111.flexypixel.data.model.PanelOrientation

@Entity(tableName = "panel_templates")
internal data class PanelTemplate(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val pixels: List<DrawingColor>,
    val orientations: List<PanelOrientation>,
)