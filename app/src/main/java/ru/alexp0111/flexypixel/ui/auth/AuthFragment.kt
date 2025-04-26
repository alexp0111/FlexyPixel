package ru.alexp0111.flexypixel.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

internal class AuthFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        composeView {
            setContentAndStrategy {
                AuthScreen(viewModel)
            }
        }

    companion object {
        fun newInstance(): AuthFragment = AuthFragment()
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}