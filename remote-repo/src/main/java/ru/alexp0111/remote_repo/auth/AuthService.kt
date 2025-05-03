package ru.alexp0111.remote_repo.auth

import retrofit2.http.Body
import retrofit2.http.POST
import ru.alexp0111.remote_repo.auth.model.ApiAuthToken
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel
import ru.alexp0111.remote_repo.token.model.ApiRefreshToken
import ru.alexp0111.remote_repo.token.model.ApiTokenPair

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginModel: ApiLoginModel): ApiAuthToken?

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshToken: ApiRefreshToken): ApiTokenPair?
}