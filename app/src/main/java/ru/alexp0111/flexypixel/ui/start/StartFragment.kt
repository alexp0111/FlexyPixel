package ru.alexp0111.flexypixel.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.alexp0111.flexypixel.databinding.FragmentStartBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.navigation.Screens
import javax.inject.Inject

private const val TAG = "StartFragment"
private const val HAS_SAVED_DEVICE = false

class StartFragment : Fragment() {

    @Inject
    lateinit var router: Router

    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        resolveNextScreen()
        super.onCreate(savedInstanceState)
    }

    private fun resolveNextScreen() {
        if (HAS_SAVED_DEVICE) {
            // TODO: Navigate to menu screen directly
        } else {
            router.newRootChain(
                Screens.SearchBluetoothDevicesScreen(),
                Screens.BluetoothResolverScreen(),
            )
        }
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