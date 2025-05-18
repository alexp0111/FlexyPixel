package ru.alexp0111.flexypixel.ui.profile.model

import android.net.Uri

internal data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val email: String = "",
    val registerDate: String = "",
    val profileUri: Uri? = null,
)