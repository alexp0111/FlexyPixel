package ru.alexp0111.flexypixel.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.alexp0111.core.di.AppScope

@Module
class AppModule(
    private val application: Application,
) {

    @Provides
    @AppScope
    fun provideContext(): Context = this.application
}