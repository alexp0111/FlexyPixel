package ru.alexp0111.flexypixel.ui.auth.model

import android.net.Uri

internal data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val profileUri: Uri? = null,
)