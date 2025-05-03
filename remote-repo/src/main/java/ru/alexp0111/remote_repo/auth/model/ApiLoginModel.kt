package ru.alexp0111.remote_repo.auth.model

data class ApiLoginModel(
    val loginOrEmail: String? = null,
    val password: String? = null,
)