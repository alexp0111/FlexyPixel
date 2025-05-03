package ru.alexp0111.remote_repo.auth.repository

import ru.alexp0111.remote_repo.auth.model.ApiAuthToken
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel
import ru.alexp0111.remote_repo.auth.model.ApiPublicId
import ru.alexp0111.remote_repo.auth.model.ApiRegisterModel

interface IAuthRepository {
    suspend fun login(loginModel: ApiLoginModel): ApiAuthToken?
    suspend fun register(registerModel: ApiRegisterModel): ApiPublicId?
}