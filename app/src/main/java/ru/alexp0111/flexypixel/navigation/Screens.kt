package ru.alexp0111.flexypixel.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.alexp0111.flexypixel.ui.DisplayLevelFragment
import ru.alexp0111.flexypixel.ui.UpperAbstractionLevelFragment
import ru.alexp0111.flexypixel.ui.menu.MenuFragment
import ru.alexp0111.flexypixel.ui.start.resolve_screen.StartFragment
import ru.alexp0111.flexypixel.ui.start.device_pairing.SearchBluetoothDevicesFragment
import ru.alexp0111.flexypixel.ui.test.TestFragment
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment

object Screens {

    fun DisplayLevelScreen() = FragmentScreen{
        DisplayLevelFragment()
    }

    fun UpperAbstractionLevelScreen() = FragmentScreen{
        UpperAbstractionLevelFragment()
    }
    fun StartScreen() = FragmentScreen {
        StartFragment()
    }

    fun SearchBluetoothDevicesScreen() = FragmentScreen {
        SearchBluetoothDevicesFragment()
    }

    fun BluetoothResolverScreen() = FragmentScreen {
        BluetoothResolverFragment()
    }

    fun MenuScreen() = FragmentScreen {
        MenuFragment()
    }

    fun TestScreen() = FragmentScreen {
        TestFragment()
    }
}