package ru.alexp0111.flexypixel.business.profile

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.alexp0111.flexypixel.business.profile.model.UserModel
import ru.alexp0111.remote_repo.profile.model.ApiAvatarModel
import ru.alexp0111.remote_repo.profile.repository.IProfileRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileRepository: IProfileRepository,
    private val context: Context,
) : IProfileInteractor {

    override suspend fun getUser(): UserModel {
        val apiUser = profileRepository.getUser()
        return UserModel(
            displayName = apiUser?.displayName.orEmpty(),
            email = apiUser?.email.orEmpty(),
            registerDate = getFormattedDate(apiUser?.createdAt.orEmpty()),
            profileUri = apiUser?.avatarUrl?.toUri(),
        )
    }

    override suspend fun loadProfilePicture(uri: Uri): ApiAvatarModel? {
        val multiPart = withContext(Dispatchers.IO) { prepareFilePart(uri) }
        return profileRepository.loadProfilePicture(multiPart)
    }

    private fun prepareFilePart(uri: Uri): MultipartBody.Part? {
        val file = convertUriToFile(uri) ?: return null
        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun convertUriToFile(uri: Uri): File? =
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val outputDir = context.cacheDir
                val fileName = buildString {
                    append("profile_picture_")
                    append(System.currentTimeMillis().toString())
                    append('.')
                    append(getFileExtension(uri))
                }
                val file = File(outputDir, fileName)
                input.copyTo(file.outputStream())
                file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val mimeType = contentResolver.getType(uri)
        return mimeTypeMap.getExtensionFromMimeType(mimeType)
    }

    private fun getFormattedDate(backedDate: String): String {
        val backendFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.US)
        val mobileFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return mobileFormat.format(backendFormat.parse(backedDate) ?: "")
    }
}