package ru.alexp0111.flexypixel.ui.templates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.templates.screen.TemplatesScreen
import javax.inject.Inject


internal class TemplatesFragment : Fragment() {

    @Inject
    lateinit var viewModel: TemplatesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeView {
            setContentAndStrategy {
                TemplatesScreen(viewModel)
            }
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(): TemplatesFragment = TemplatesFragment()
    }
}