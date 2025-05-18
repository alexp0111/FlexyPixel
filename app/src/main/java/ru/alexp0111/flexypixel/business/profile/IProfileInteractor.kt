package ru.alexp0111.flexypixel.business.profile

import android.net.Uri
import ru.alexp0111.flexypixel.business.profile.model.UserModel
import ru.alexp0111.remote_repo.profile.model.ApiAvatarModel

interface IProfileInteractor {
    suspend fun getUser(): UserModel
    suspend fun loadProfilePicture(uri: Uri): ApiAvatarModel?
}