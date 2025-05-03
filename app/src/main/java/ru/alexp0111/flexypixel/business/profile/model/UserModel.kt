package ru.alexp0111.flexypixel.business.profile.model

import android.net.Uri

data class UserModel(
    val username: String,
    val email: String,
    val registerDate: String,
    val profileUri: Uri?,
)