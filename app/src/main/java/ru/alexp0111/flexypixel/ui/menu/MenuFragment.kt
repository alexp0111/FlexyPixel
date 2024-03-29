package ru.alexp0111.flexypixel.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.alexp0111.flexypixel.databinding.FragmentMenuBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.navigation.Screens
import javax.inject.Inject

private const val TAG = "MenuFragment"

class MenuFragment : Fragment() {

    @Inject
    lateinit var router: Router

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater)
        binding.apply {
            btnTests.setOnClickListener {
                router.navigateTo(Screens.TestScreen())
            }
        }
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