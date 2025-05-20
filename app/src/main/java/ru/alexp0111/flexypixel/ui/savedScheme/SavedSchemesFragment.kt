package ru.alexp0111.flexypixel.ui.savedScheme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.business.GlobalStateHandler
import javax.inject.Inject

internal class SavedSchemesFragment : Fragment() {

    @Inject
    lateinit var viewModel: SavedSchemesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    composeView {
        setContentAndStrategy {
            SavedSchemeScreen(viewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalStateHandler.reset()
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

}