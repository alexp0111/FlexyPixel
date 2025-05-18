package ru.alexp0111.remote_repo.profile.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import ru.alexp0111.remote_repo.profile.ProfileService
import ru.alexp0111.remote_repo.profile.model.ApiAvatarModel
import ru.alexp0111.remote_repo.profile.model.ApiUser
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val service: ProfileService,
) : IProfileRepository {
    override suspend fun getUser(): ApiUser? =
        withContext(Dispatchers.IO) { service.getProfile() }

    override suspend fun loadProfilePicture(multiPart: MultipartBody.Part?): ApiAvatarModel? =
        withContext(Dispatchers.IO) { service.loadProfilePicture(multiPart) }
}