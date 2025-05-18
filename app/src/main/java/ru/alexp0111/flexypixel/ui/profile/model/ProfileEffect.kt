package ru.alexp0111.flexypixel.ui.profile.model

internal sealed interface ProfileEffect {
    data class ShowSnackBar(val message: String): ProfileEffect
}