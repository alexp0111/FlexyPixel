package ru.alexp0111.flexypixel.business.profile

import ru.alexp0111.flexypixel.business.profile.model.UserModel

interface IProfileInteractor {
    suspend fun getUser(): UserModel
}