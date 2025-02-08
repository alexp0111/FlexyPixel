package ru.alexp0111.flexypixel.di.components

import androidx.fragment.app.Fragment

import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelFragment
import ru.alexp0111.flexypixel.ui.drawing.DrawingFragment
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelFragment

import ru.alexp0111.flexypixel.ui.menu.MenuFragment
import ru.alexp0111.flexypixel.ui.savedScheme.SavedSchemesFragment
import ru.alexp0111.flexypixel.ui.start.device_pairing.SearchBluetoothDevicesFragment
import ru.alexp0111.flexypixel.ui.start.resolve_screen.StartFragment
import ru.alexp0111.flexypixel.ui.test.TestFragment
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment

internal interface FragmentComponent {

    fun inject(fragment: UpperAbstractionLevelFragment)
    fun inject(fragment: StartFragment)
    fun inject(fragment: SearchBluetoothDevicesFragment)
    fun inject(fragment: BluetoothResolverFragment)
    fun inject(fragment: MenuFragment)
    fun inject(fragment: TestFragment)
    fun inject(fragment: DisplayLevelFragment)
    fun inject(drawingFragment: DrawingFragment)
    fun inject(savedSchemesFragment: SavedSchemesFragment)

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}