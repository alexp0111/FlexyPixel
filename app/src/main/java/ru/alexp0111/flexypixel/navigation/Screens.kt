package ru.alexp0111.flexypixel.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.alexp0111.flexypixel.ui.start.device_pairing.SearchBluetoothDevicesFragment
import ru.alexp0111.flexypixel.ui.start.StartFragment
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment

object Screens {
    fun StartScreen() = FragmentScreen {
        StartFragment()
    }

    fun SearchBluetoothDevicesScreen() = FragmentScreen {
        SearchBluetoothDevicesFragment()
    }

    fun BluetoothResolverScreen() = FragmentScreen {
        BluetoothResolverFragment()
    }
}