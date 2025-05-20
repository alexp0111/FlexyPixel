package ru.alexp0111.flexypixel.ui.savedScheme.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class SavedSchemeUiState(
    val isLoading: Boolean = false,
    val schemeItems: List<SavedSchemeUiModel> = emptyList(),
)

@Immutable
data class SavedSchemeUiModel(
    val uuid: String,
    val title: String,
    val createdAt: String,
    val preview: Uri? = null,
)