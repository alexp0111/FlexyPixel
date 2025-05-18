package ru.alexp0111.flexypixel.ui.auth.model

internal sealed interface AuthEffect {
    data class ShowSnackBar(val message: String): AuthEffect
}