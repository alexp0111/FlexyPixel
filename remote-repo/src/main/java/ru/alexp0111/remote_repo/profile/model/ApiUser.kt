package ru.alexp0111.remote_repo.profile.model

data class ApiUser(
    val publicId: String? = null,
    val email: String? = null,
    val username: String? = null,
    val displayName: String? = null,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val isVerified: String? = null,
    val role: String? = null,
    val mobileRole: String? = null,
    val createdAt: String? = null,
)