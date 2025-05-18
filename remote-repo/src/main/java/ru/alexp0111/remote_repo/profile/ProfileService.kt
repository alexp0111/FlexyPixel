package ru.alexp0111.remote_repo.profile

import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import ru.alexp0111.remote_repo.profile.model.ApiAvatarModel
import ru.alexp0111.remote_repo.profile.model.ApiUser

interface ProfileService {
    @GET("users/me")
    suspend fun getProfile(): ApiUser?

    @Multipart
    @PATCH("users/me/avatar")
    suspend fun loadProfilePicture(
        @Part file: MultipartBody.Part?
    ): ApiAvatarModel?
}