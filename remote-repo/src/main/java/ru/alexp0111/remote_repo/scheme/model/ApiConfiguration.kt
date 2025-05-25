package ru.alexp0111.remote_repo.scheme.model

data class ApiConfiguration(
    val publicId: String? = null,
    val name: String? = null,
    val frameInfo: List<ApiFrameInfo>,
    val miniPreviewImageUrl: String? = null,
    val interFrameDelay: Int? = null,
    val panelsInfo: List<ApiPanelsInfo>
)