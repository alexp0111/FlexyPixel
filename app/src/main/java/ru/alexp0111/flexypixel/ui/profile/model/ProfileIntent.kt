package ru.alexp0111.flexypixel.ui.profile.model

internal sealed interface ProfileIntent {
    data object OnExitClicked : ProfileIntent
}