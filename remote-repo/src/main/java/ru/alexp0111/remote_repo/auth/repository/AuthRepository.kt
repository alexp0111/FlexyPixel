package ru.alexp0111.remote_repo.auth.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexp0111.remote_repo.auth.AuthService
import ru.alexp0111.remote_repo.auth.model.ApiAuthToken
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AuthService,
) : IAuthRepository {
    override suspend fun login(loginModel: ApiLoginModel): ApiAuthToken? =
        withContext(Dispatchers.IO) { service.login(loginModel) }
}