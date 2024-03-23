package ru.alexp0111.flexypixel.di.components

import androidx.fragment.app.Fragment
import ru.alexp0111.flexypixel.ui.menu.MenuFragment
import ru.alexp0111.flexypixel.ui.start.device_pairing.SearchBluetoothDevicesFragment
import ru.alexp0111.flexypixel.ui.start.StartFragment
import ru.alexp0111.flexypixel.ui.test.TestFragment
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment

interface FragmentComponent {

    fun inject(fragment: StartFragment)
    fun inject(fragment: SearchBluetoothDevicesFragment)
    fun inject(fragment: BluetoothResolverFragment)
    fun inject(fragment: MenuFragment)
    fun inject(fragment: TestFragment)

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}