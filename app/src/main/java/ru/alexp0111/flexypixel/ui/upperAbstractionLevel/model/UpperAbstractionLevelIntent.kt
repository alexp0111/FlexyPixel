package ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model

sealed interface UpperAbstractionLevelIntent {
    data class TitleChanged(val newTitle: String): UpperAbstractionLevelIntent
    data class CardClicked(val x: Int, val y: Int): UpperAbstractionLevelIntent
}