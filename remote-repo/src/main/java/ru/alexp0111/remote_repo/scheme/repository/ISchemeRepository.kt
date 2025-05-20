package ru.alexp0111.remote_repo.scheme.repository

import ru.alexp0111.remote_repo.scheme.model.ApiScheme

interface ISchemeRepository {
    suspend fun getSchemeList(): List<ApiScheme>
}