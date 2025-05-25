package ru.alexp0111.remote_repo.scheme

import retrofit2.http.GET
import retrofit2.http.Path
import ru.alexp0111.remote_repo.scheme.model.ApiConfiguration
import ru.alexp0111.remote_repo.scheme.model.ApiScheme

interface SchemeService {
    @GET("/mobile/configurations/all")
    suspend fun getSchemeList(): List<ApiScheme>?

    @GET("/mobile/configurations/{id}")
    suspend fun getConfigById(@Path("id") id: String): ApiConfiguration?
}