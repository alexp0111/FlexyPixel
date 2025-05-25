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
private const val SCHEME_PREVIEW_KEY = "SCHEME_PREVIEW_KEY"
private const val SCHEME_DATE_KEY = "SCHEME_DATE_KEY"

internal class UpperAbstractionLevelFragment : Fragment() {

    @Inject
    internal lateinit var viewModel: UpperAbstractionLevelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        val schemeId = arguments?.getString(SCHEME_ID_KEY)
        val preview = arguments?.getString(SCHEME_PREVIEW_KEY)
        val date = arguments?.getString(SCHEME_DATE_KEY)
        viewModel.sendIntent(UpperAbstractionLevelIntent.InitScheme(schemeId, preview, date))
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
        fun newInstance(schemeId: String?, preview: String?, createdAt: String?): UpperAbstractionLevelFragment {
            return UpperAbstractionLevelFragment().apply {
                    arguments = Bundle().apply {
                        schemeId?.let { putString(SCHEME_ID_KEY, it) }
                        preview?.let { putString(SCHEME_PREVIEW_KEY, it) }
                        createdAt?.let { putString(SCHEME_DATE_KEY, it) }
                    }
            }
        }
    }
}