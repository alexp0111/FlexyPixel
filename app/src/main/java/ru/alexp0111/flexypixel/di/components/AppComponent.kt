package ru.alexp0111.flexypixel.di.components

import android.content.Context
import dagger.Component
import ru.alexp0111.bt_service.di.BluetoothBindsModule
import ru.alexp0111.core.di.AppScope
import ru.alexp0111.flexypixel.FlexyPixelApplication
import ru.alexp0111.flexypixel.di.modules.AppModule
import ru.alexp0111.flexypixel.di.modules.BindModule
import ru.alexp0111.bt_service.di.BluetoothModule
import ru.alexp0111.di.MediaBindsModule
import ru.alexp0111.flexypixel.di.modules.NavigationModule
import ru.alexp0111.local_repo.di.DatabaseModule
import ru.alexp0111.remote_repo.di.RemoteRepoBindsModule
import ru.alexp0111.remote_repo.di.RemoteRepoModule

@AppScope
@Component(
    modules = [
        AppModule::class,
        NavigationModule::class,
        BluetoothModule::class,
        BluetoothBindsModule::class,
        DatabaseModule::class,
        RemoteRepoModule::class,
        RemoteRepoBindsModule::class,
        MediaBindsModule::class,
        BindModule::class,
    ]
)
internal interface AppComponent :
    ActivityComponent,
    FragmentComponent {

    companion object {
        fun from(context: Context): AppComponent {
            return (context.applicationContext as FlexyPixelApplication).appComponent
        }
    }
}