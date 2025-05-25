package ru.alexp0111.flexypixel.business.saved_scheme

import androidx.core.net.toUri
import ru.alexp0111.flexypixel.business.saved_scheme.model.SavedSchemeBusinessModel
import ru.alexp0111.local_repo.database.schemes.SavedSchemeRepository
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import ru.alexp0111.remote_repo.scheme.model.ApiScheme
import ru.alexp0111.remote_repo.scheme.repository.ISchemeRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SavedSchemeInteractor @Inject constructor(
    private val databaseRepository: SavedSchemeRepository,
    private val schemeRepository: ISchemeRepository,
) : ISavedSchemeInteractor {
    override suspend fun getAllSchemes(): List<SavedSchemeBusinessModel> {
        val localSchemes = databaseRepository.getAllSchemes().map(::localModelToBusiness)
        val remoteSchemes = runCatching {
            schemeRepository.getSchemeList().filter { it.publicId != null }.map(::remoteToBusiness)
        }.getOrNull() ?: return localSchemes
        val remoteSchemeIds = remoteSchemes.map { it.uuid }.toSet()

        return buildList {
            addAll(remoteSchemes)
            localSchemes.forEach {
                if (it.uuid !in remoteSchemeIds) add(it)
            }
        }
    }

    private fun localModelToBusiness(dataModel: UserSavedScheme): SavedSchemeBusinessModel =
        SavedSchemeBusinessModel(
            uuid = dataModel.id,
            title = dataModel.title,
            preview = dataModel.preview?.toUri(),
            createdAt = dataModel.createdAt
        )

    private fun remoteToBusiness(dataModel: ApiScheme): SavedSchemeBusinessModel =
        SavedSchemeBusinessModel(
            uuid = dataModel.publicId!!,
            title = dataModel.name,
            preview = dataModel.miniPreviewImageUrl?.toUri(),
            createdAt = dataModel.createdAt?.let(::getFormattedDate),
        )

    private fun getFormattedDate(backedDate: String): String {
        val backendFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.US)
        val mobileFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return mobileFormat.format(backendFormat.parse(backedDate) ?: "")
    }
}