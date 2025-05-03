package ru.alexp0111.remote_repo.auth.repository

import ru.alexp0111.remote_repo.auth.model.ApiAuthToken
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel

interface IAuthRepository {
    suspend fun login(loginModel: ApiLoginModel): ApiAuthToken?
}