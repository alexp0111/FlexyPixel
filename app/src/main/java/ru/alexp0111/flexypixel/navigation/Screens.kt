package ru.alexp0111.flexypixel.navigation


import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelFragment
import ru.alexp0111.flexypixel.ui.drawing.DrawingFragment
import ru.alexp0111.flexypixel.ui.menu.MenuFragment
import ru.alexp0111.flexypixel.ui.savedScheme.SavedSchemesFragment
import ru.alexp0111.flexypixel.ui.start.device_pairing.SearchBluetoothDevicesFragment
import ru.alexp0111.flexypixel.ui.start.resolve_screen.StartFragment
import ru.alexp0111.flexypixel.ui.templates.TemplatesFragment
import ru.alexp0111.flexypixel.ui.test.TestFragment
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.UpperAbstractionLevelFragment
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment

object Screens {

    fun TemplatesScreen() = FragmentScreen {
        TemplatesFragment()
    }

    fun DisplayLevelScreen(segmentNumber: Int) = FragmentScreen("DisplayLevelScreen_$segmentNumber") {
        DisplayLevelFragment.newInstance(segmentNumber)
    }

    fun UpperAbstractionLevelScreen(schemeId: Int) = FragmentScreen("UpperAbstractionLevelScreen_$schemeId") {
        UpperAbstractionLevelFragment.newInstance(schemeId)
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

    fun DrawingScreen(panelNumber: Int) = FragmentScreen("DrawingScreen_$panelNumber") {
        DrawingFragment.newInstance(panelNumber)
    }

    fun SavedSchemesScreen() = FragmentScreen {
        SavedSchemesFragment()
    }
}