package ru.alexp0111.flexypixel.di.components

import android.content.Context
import dagger.Component
import ru.alexp0111.flexypixel.FlexyPixelApplication
import ru.alexp0111.flexypixel.di.modules.AppModule
import ru.alexp0111.flexypixel.di.modules.NavigationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NavigationModule::class,
    ]
)
interface AppComponent :
    ActivityComponent,
    FragmentComponent {

    companion object {
        fun from(context: Context): AppComponent {
            return (context.applicationContext as FlexyPixelApplication).appComponent
        }
    }
}