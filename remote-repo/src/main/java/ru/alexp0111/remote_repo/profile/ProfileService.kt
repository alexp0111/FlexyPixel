package ru.alexp0111.remote_repo.profile

import retrofit2.http.GET
import ru.alexp0111.remote_repo.profile.model.ApiUser

interface ProfileService {
    @GET("users/me")
    suspend fun getProfile(): ApiUser?
}