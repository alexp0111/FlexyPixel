package ru.alexp0111.flexypixel.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

internal class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModel: ProfileViewModel

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
                ProfileScreen(viewModel)
            }
        }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}