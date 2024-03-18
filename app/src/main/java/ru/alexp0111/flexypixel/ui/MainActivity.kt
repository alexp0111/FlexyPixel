package ru.alexp0111.flexypixel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.bluetooth.AndroidBluetoothController
import ru.alexp0111.flexypixel.di.components.ActivityComponent
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.util.BluetoothResolverFragment
import ru.alexp0111.flexypixel.util.PermissionResolver
import javax.inject.Inject

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var permissionResolver: PermissionResolver

    @Inject
    lateinit var controller: AndroidBluetoothController

    private val navigator: Navigator = AppNavigator(this, R.id.container_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        router.replaceScreen(Screens.StartScreen())
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.release()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container_main)
        if (fragment != null && fragment is BluetoothResolverFragment
            && !permissionResolver.isBluetoothAvailable()
        ) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun injectSelf() {
        ActivityComponent.from(this).inject(this)
    }
}