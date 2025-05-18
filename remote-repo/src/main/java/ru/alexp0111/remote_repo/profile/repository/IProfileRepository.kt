package ru.alexp0111.remote_repo.profile.repository

import okhttp3.MultipartBody
import ru.alexp0111.remote_repo.profile.model.ApiAvatarModel
import ru.alexp0111.remote_repo.profile.model.ApiUser

interface IProfileRepository {
    suspend fun getUser(): ApiUser?
    suspend fun loadProfilePicture(multiPart: MultipartBody.Part?): ApiAvatarModel?
}