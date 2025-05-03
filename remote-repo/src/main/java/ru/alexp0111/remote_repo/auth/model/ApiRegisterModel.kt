package ru.alexp0111.remote_repo.auth.model

data class ApiRegisterModel(
    val email: String?,
    val login: String?,
    val displayName: String?,
    val password: String?,
)