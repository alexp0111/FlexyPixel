package ru.alexp0111.remote_repo.token.model

data class ApiTokenPair(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)