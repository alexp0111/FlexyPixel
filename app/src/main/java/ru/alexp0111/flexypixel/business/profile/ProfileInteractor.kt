package ru.alexp0111.flexypixel.business.profile

import androidx.core.net.toUri
import ru.alexp0111.flexypixel.business.profile.model.UserModel
import ru.alexp0111.remote_repo.profile.repository.ProfileRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileRepository: ProfileRepository,
) : IProfileInteractor {

    override suspend fun getUser(): UserModel {
        val apiUser = profileRepository.getUser()
        return UserModel(
            username = apiUser?.username.orEmpty(),
            email = apiUser?.email.orEmpty(),
            registerDate = getFormattedDate(apiUser?.createdAt.orEmpty()),
            profileUri = apiUser?.avatarUrl?.toUri(),
        )
    }

    private fun getFormattedDate(backedDate: String): String {
        val backendFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.US)
        val mobileFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return mobileFormat.format(backendFormat.parse(backedDate) ?: "")
    }
}