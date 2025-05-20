package ru.alexp0111.remote_repo.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.alexp0111.core.di.AppScope
import ru.alexp0111.remote_repo.BuildConfig
import ru.alexp0111.remote_repo.auth.AuthService
import ru.alexp0111.remote_repo.auth.repository.AuthRepository
import ru.alexp0111.remote_repo.auth.repository.IAuthRepository
import ru.alexp0111.remote_repo.profile.ProfileService
import ru.alexp0111.remote_repo.profile.repository.IProfileRepository
import ru.alexp0111.remote_repo.profile.repository.ProfileRepository
import ru.alexp0111.remote_repo.scheme.SchemeService
import ru.alexp0111.remote_repo.scheme.repository.ISchemeRepository
import ru.alexp0111.remote_repo.scheme.repository.SchemeRepository
import ru.alexp0111.remote_repo.token.TokenInterceptor
import ru.alexp0111.remote_repo.util.PrettyLogger


@Module
class RemoteRepoModule {
    @AppScope
    @AuthRetrofit
    @Provides
    fun provideAuthRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor(PrettyLogger()).apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScope
    @TokenRetrofit
    @Provides
    fun provideTokenRetrofit(tokenInterceptor: TokenInterceptor): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor(PrettyLogger()).apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @AppScope
    @Provides
    fun provideProfileService(@TokenRetrofit retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @AppScope
    @Provides
    fun provideSchemeService(@TokenRetrofit retrofit: Retrofit): SchemeService =
        retrofit.create(SchemeService::class.java)
}

@Module
interface RemoteRepoBindsModule {
    @AppScope
    @Binds
    fun bindAuthRepository(repo: AuthRepository): IAuthRepository

    @AppScope
    @Binds
    fun bindProfileRepository(repo: ProfileRepository): IProfileRepository

    @AppScope
    @Binds
    fun bindSchemeRepository(repo: SchemeRepository): ISchemeRepository
}