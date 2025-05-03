package ru.alexp0111.remote_repo.token

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.alexp0111.remote_repo.auth.AuthService
import ru.alexp0111.remote_repo.token.model.ApiRefreshToken
import javax.inject.Inject


class TokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val accessToken =  tokenManager.getToken(TokenManager.ACCESS) ?: ""

        if (accessToken.isNotEmpty()) {
            var request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            try {
                val response = chain.proceed(request)

                if (response.code == 401) {
                    updateToken()
                    val newAccessToken = tokenManager.getToken(TokenManager.ACCESS).orEmpty()

                    request = chain.request()
                        .newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    return@runBlocking chain.proceed(request)
                } else {
                    return@runBlocking response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@runBlocking chain.proceed(chain.request())
    }

    private suspend fun updateToken() {
        val refreshToken = tokenManager.getToken(TokenManager.REFRESH)
        val newTokenPair = authService.refreshToken(ApiRefreshToken(refreshToken))
        tokenManager.apply {
            newTokenPair?.accessToken?.let { saveToken(TokenManager.ACCESS, it) }
            newTokenPair?.refreshToken?.let { saveToken(TokenManager.REFRESH, it) }
        }
    }
}