package ru.alexp0111.remote_repo.di

import javax.inject.Qualifier


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AuthRetrofit

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class TokenRetrofit