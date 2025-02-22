package ru.alexp0111.flexypixel.ui.displayLevel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.core_ui.util.composeView
import ru.alexp0111.core_ui.util.setContentAndStrategy
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.screen.DisplayLevelScreen
import javax.inject.Inject

private const val SEGMENT_NUMBER_KEY = "SEGMENT_NUMBER_KEY"

internal class DisplayLevelFragment : Fragment() {

    @Inject
    lateinit var viewModel: DisplayLevelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val segmentNumber = arguments?.getInt(SEGMENT_NUMBER_KEY) ?: 0
        viewModel.sendIntent(DisplayLevelIntent.InitSegment(segmentNumber))
        return composeView {
            setContentAndStrategy {
                DisplayLevelScreen(viewModel)
            }
        }
    }

    companion object {
        fun newInstance(segmentNumber: Int): DisplayLevelFragment {
            return DisplayLevelFragment().apply {
                arguments = Bundle().apply {
                    putInt(SEGMENT_NUMBER_KEY, segmentNumber)
                }
            }
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}