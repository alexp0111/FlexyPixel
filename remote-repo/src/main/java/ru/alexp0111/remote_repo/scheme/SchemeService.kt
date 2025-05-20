package ru.alexp0111.remote_repo.scheme

import retrofit2.http.GET
import ru.alexp0111.remote_repo.scheme.model.ApiScheme

interface SchemeService {
    @GET("/mobile/configurations/all")
    suspend fun getSchemeList(): List<ApiScheme>?
}