package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

sealed interface UpperAbstractionLevelIntent {
    data class TitleChanged(val newTitle: String): UpperAbstractionLevelIntent
    data class CardClicked(val x: Int, val y: Int): UpperAbstractionLevelIntent
    data class InitScheme(val schemeId: Int?): UpperAbstractionLevelIntent
    data object Save: UpperAbstractionLevelIntent
}