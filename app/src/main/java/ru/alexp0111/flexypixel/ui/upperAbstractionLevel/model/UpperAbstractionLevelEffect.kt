package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

internal sealed interface UpperAbstractionLevelEffect {
    data class ShowSnackBar(val message: String): UpperAbstractionLevelEffect
}