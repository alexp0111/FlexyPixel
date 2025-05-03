package ru.alexp0111.remote_repo.auth.model

data class ApiAuthToken(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val publicId: String? = null,
)