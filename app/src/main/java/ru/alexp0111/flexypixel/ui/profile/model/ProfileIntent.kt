package ru.alexp0111.flexypixel.ui.profile.model

import android.net.Uri

internal sealed interface ProfileIntent {
    data object OnExitClicked : ProfileIntent
    data class OnProfilePictureChosen(val uri: Uri) : ProfileIntent
}