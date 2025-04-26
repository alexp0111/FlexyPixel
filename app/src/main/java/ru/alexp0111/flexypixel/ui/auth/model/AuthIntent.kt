package ru.alexp0111.flexypixel.ui.auth.model

internal sealed interface AuthIntent {
    data class OnUsernameChanged(val username: String) : AuthIntent
    data class OnPasswordChanged(val password: String) : AuthIntent
    data class OnEmailChanged(val password: String) : AuthIntent
    data object OnLoginClicked : AuthIntent
    data object OnRegisterClicked : AuthIntent
    data object OnResetPasswordClicked : AuthIntent
}