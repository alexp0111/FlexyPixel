package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

sealed interface UpperAbstractionLevelEffect {
    data class ShowSnackBar(val message: String): UpperAbstractionLevelEffect
}