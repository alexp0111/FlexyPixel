package ru.alexp0111.flexypixel.ui.auth.model

import android.net.Uri

internal data class AuthUiState(
    val isLoading: Boolean = false,
    val type: AuthScreenType = AuthScreenType.LOGIN,
    val login: String = "",
    val displayName: String = "",
    val password: String = "",
    val email: String = "",
    val profileUri: Uri? = null,
)

internal enum class AuthScreenType {
    LOGIN,
    REGISTER;
}