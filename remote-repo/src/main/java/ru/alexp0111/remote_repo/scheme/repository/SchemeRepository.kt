package ru.alexp0111.remote_repo.scheme.repository

import ru.alexp0111.remote_repo.scheme.SchemeService
import ru.alexp0111.remote_repo.scheme.model.ApiScheme
import javax.inject.Inject

class SchemeRepository @Inject constructor(
    private val schemeService: SchemeService,
) : ISchemeRepository {
    override suspend fun getSchemeList(): List<ApiScheme> =
        schemeService.getSchemeList() ?: emptyList()
}