package ru.alexp0111.flexypixel.database.templates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.alexp0111.flexypixel.database.templates.model.PanelTemplate

@Dao
internal interface PanelTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: PanelTemplate): Long

    @Update
    suspend fun updateTemplate(template: PanelTemplate)

    @Query("SELECT * FROM panel_templates")
    suspend fun getAllTemplates(): List<PanelTemplate>

}