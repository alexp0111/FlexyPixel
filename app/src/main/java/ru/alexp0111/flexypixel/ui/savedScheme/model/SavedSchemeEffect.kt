package ru.alexp0111.flexypixel.ui.savedScheme.model

internal sealed interface SavedSchemeEffect {
    data class ShowSnackBar(val message: String): SavedSchemeEffect
}