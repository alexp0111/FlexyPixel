package ru.alexp0111.flexypixel.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.business.GlobalStateHandler
import javax.inject.Inject

class MenuFragment : Fragment() {

    @Inject
    lateinit var menuViewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        composeView {
            setContentAndStrategy {
                MenuScreen(menuViewModel)
            }
        }

    override fun onStart() {
        super.onStart()
        GlobalStateHandler.reset()
    }

    private fun injectSelf() = FragmentComponent.from(this).inject(this)
}