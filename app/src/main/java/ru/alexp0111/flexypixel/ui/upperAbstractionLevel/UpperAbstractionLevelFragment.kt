package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import javax.inject.Inject

private const val SCHEME_ID_KEY = "SCHEME_ID_KEY"

class UpperAbstractionLevelFragment : Fragment() {

    @Inject
    lateinit var viewModel: UpperAbstractionLevelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        val schemeId = arguments?.getInt(SCHEME_ID_KEY)
        viewModel.sendIntent(UpperAbstractionLevelIntent.InitScheme(schemeId))
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeView {
            setContentAndStrategy {
                UpperAbstractionLevelScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.sendIntent(UpperAbstractionLevelIntent.RefreshSegmentInfo)
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(schemeId: Int?): UpperAbstractionLevelFragment {
            return UpperAbstractionLevelFragment().apply {
                schemeId?.let {
                    arguments = Bundle().apply {
                        putInt(SCHEME_ID_KEY, it)
                    }
                }
            }
        }
    }
}