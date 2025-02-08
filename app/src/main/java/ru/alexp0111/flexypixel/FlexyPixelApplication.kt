package ru.alexp0111.flexypixel

import android.app.Application
import ru.alexp0111.flexypixel.di.components.AppComponent
import ru.alexp0111.flexypixel.di.components.DaggerAppComponent
import ru.alexp0111.flexypixel.di.modules.AppModule

internal class FlexyPixelApplication : Application() {

    val appComponent: AppComponent by lazy {
        initComponent()
    }

    private fun initComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}