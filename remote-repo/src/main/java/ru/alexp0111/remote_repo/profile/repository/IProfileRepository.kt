package ru.alexp0111.remote_repo.profile.repository

import ru.alexp0111.remote_repo.profile.model.ApiUser

interface IProfileRepository {
    suspend fun getUser(): ApiUser?
}