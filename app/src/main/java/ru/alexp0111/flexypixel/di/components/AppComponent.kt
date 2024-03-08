package ru.alexp0111.flexypixel.di.components

import android.content.Context
import dagger.Component
import ru.alexp0111.flexypixel.FlexyPixelApplication
import ru.alexp0111.flexypixel.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
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