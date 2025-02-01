package ru.alexp0111.flexypixel.ui.start.resolve_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.alexp0111.flexypixel.bluetooth.AndroidBluetoothController
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.util.PermissionResolver
import javax.inject.Inject

private const val DEBUG_ONLY_SKIP_CONNECTION = false

class StartFragmentViewModel @Inject constructor(
    private val controller: AndroidBluetoothController,
    private val permissionResolver: PermissionResolver,
    private val router: Router,
) : ViewModel() {
    fun resolveNextScreen() {
        if (DEBUG_ONLY_SKIP_CONNECTION) {
            goToMenu()
            return
        }

        if (!permissionResolver.isSystemCompletelyReady()) {
            goToSearchScreen()
            return
        }

        viewModelScope.plus(Dispatchers.IO).launch {
            controller.isConnectedToFPModule().collect { connectedSuccessfully ->
                if (connectedSuccessfully) {
                    goToMenu()
                } else {
                    goToSearchScreen()
                }
            }
        }
    }

    private fun goToMenu() {
        router.newRootScreen(
            Screens.MenuScreen(),
        )
    }

    private fun goToSearchScreen() {
        router.newRootChain(
            Screens.SearchBluetoothDevicesScreen(),
            Screens.BluetoothResolverScreen(),
        )
    }
}