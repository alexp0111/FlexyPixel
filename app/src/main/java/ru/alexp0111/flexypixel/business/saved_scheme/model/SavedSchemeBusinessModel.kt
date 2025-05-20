package ru.alexp0111.flexypixel.business.saved_scheme.model

import android.net.Uri

data class SavedSchemeBusinessModel(
    val uuid: String,
    val title: String?,
    val createdAt: String?,
    val preview: Uri? = null,
)