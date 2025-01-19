package ru.alexp0111.flexypixel.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import javax.inject.Inject

private const val TAG = "MenuFragment"
const val NEW_SCHEME_CODE = -1

class MenuFragment : Fragment() {

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        setContentAndStrategy {
            MenuScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.cardNewScheme.setOnClickListener {
//            router.navigateTo(Screens.UpperAbstractionLevelScreen(NEW_SCHEME_CODE))
//        }
//        binding.cardSavedScheme.setOnClickListener {
//            router.navigateTo(Screens.SavedSchemesScreen())
//        }
//        binding.cardTemplate.setOnClickListener {
//            // router.navigateTo(Screens.DrawingScreen())
//        }
    }

    override fun onStart() {
        super.onStart()
        GlobalStateHandler.reset()
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}