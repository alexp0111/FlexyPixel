package ru.alexp0111.flexypixel.ui.start.resolve_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.flexypixel.databinding.FragmentStartBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

private const val TAG = "StartFragment"

class StartFragment : Fragment() {

    @Inject
    lateinit var viewModel: StartFragmentViewModel

    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        viewModel.resolveNextScreen()
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}