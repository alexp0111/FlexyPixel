package ru.alexp0111.flexypixel.ui.savedScheme.model

sealed interface SavedSchemeIntent {
    data class SchemeItemClicked(val uuid: String) : SavedSchemeIntent
}