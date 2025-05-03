package ru.alexp0111.flexypixel.ui.auth.model

internal sealed interface AuthIntent {
    data class OnLoginChanged(val login: String) : AuthIntent
    data class OnDisplayNameChanged(val displayName: String) : AuthIntent
    data class OnPasswordChanged(val password: String) : AuthIntent
    data class OnEmailChanged(val email: String) : AuthIntent
    data object OnLoginClicked : AuthIntent
    data object OnRegisterClicked : AuthIntent
    data object OnResetPasswordClicked : AuthIntent
    data object OnGoToRegisterClicked : AuthIntent
    data object OnGoToLoginClicked : AuthIntent
}