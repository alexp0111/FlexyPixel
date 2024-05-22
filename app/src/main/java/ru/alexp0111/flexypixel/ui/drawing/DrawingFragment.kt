package ru.alexp0111.flexypixel.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDrawingBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

private const val RADIUS_RATIO = 5
private const val MARGINS_RATIO = 12
private const val PANEL_NUMBER_KEY = "PANEL_NUMBER_KEY"

class DrawingFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var stateHolderFactory: DrawingViewModelFactory

    private var stateHolder: DrawingViewModel? = null

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    private var paletteList = emptyList<MaterialCardView>()

    private val pixels = mutableListOf<MaterialCardView>()


    private val PIXEL_CARD_SIZE: Int by lazy {
        resources.getDimension(R.dimen.pixel_card_size).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        stateHolder = stateHolderFactory.create()
        val panelPosition = arguments?.getInt(PANEL_NUMBER_KEY) ?: 0
        stateHolder?.setPanelPosition(panelPosition)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillUpPaletteList()
        setPaletteOnClick()
        fillUpDrawingPanel()
        setUpSlidersOnChanged()
        stateHolder?.consumeAction(DrawingAction.RequestDisplayConfiguration)
        subscribeUI()
    }

    private fun setUpSlidersOnChanged() {
        binding.apply {
            sliderRed.addOnChangeListener { _, value, _ ->
                stateHolder?.consumeAction(
                    DrawingAction.RequestChangePaletteItemColor(ColorChannel.RED, value.toInt())
                )
            }
            sliderGreen.addOnChangeListener { _, value, _ ->
                stateHolder?.consumeAction(
                    DrawingAction.RequestChangePaletteItemColor(ColorChannel.GREEN, value.toInt())
                )
            }
            sliderBlue.addOnChangeListener { _, value, _ ->
                stateHolder?.consumeAction(
                    DrawingAction.RequestChangePaletteItemColor(ColorChannel.BLUE, value.toInt())
                )
            }
        }
    }

    private fun setPaletteOnClick() {
        for ((index, paletteItem) in paletteList.withIndex()) {
            paletteItem.setOnClickListener {
                stateHolder?.consumeAction(DrawingAction.PickPaletteItem(index))
            }
        }
    }

    private fun fillUpPaletteList() {
        binding.apply {
            paletteList = listOf(
                paletteItem1,
                paletteItem2,
                paletteItem3,
                paletteItem4,
                paletteItem5,
                paletteItem6,
                paletteItem7,
                paletteItem8,
                paletteItem9,
                paletteItem10,
                paletteItem11,
                paletteItem12,
            )
        }
    }

    private fun fillUpDrawingPanel() {
        binding.drawingGrid.apply {
            for (i in 0..63) {
                val pixel = MaterialCardView(requireContext())
                val params = GridLayout.LayoutParams()

                pixel.radius = (PIXEL_CARD_SIZE / RADIUS_RATIO).toFloat()
                params.width = PIXEL_CARD_SIZE
                params.height = PIXEL_CARD_SIZE
                params.setMargins(PIXEL_CARD_SIZE / MARGINS_RATIO)

                params.rowSpec = GridLayout.spec(i / 8)
                params.columnSpec = GridLayout.spec(i % 8)

                pixel.setCardBackgroundColor(Color.BLACK)
                pixel.strokeWidth = 0
                pixel.layoutParams = params

                pixel.setOnClickListener {
                    stateHolder?.consumeAction(DrawingAction.RequestPixelColorUpdate(pixelPosition = i))
                }

                pixels.add(pixel)
                this.addView(pixel)
            }
        }
    }


    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    stateHolder?.state?.collect { state: DrawingUIState ->

                        for ((index, pixel) in pixels.withIndex()) {
                            pixel.setCardBackgroundColor(state.pixelPanel[index].toColor())
                        }

                        for ((index, paletteItem) in paletteList.withIndex()) {
                            if (index == state.chosenPaletteItem) {
                                paletteItem.strokeWidth = 10
                                paletteItem.strokeColor =
                                    getPaletteItemStrokeColor()
                            } else {
                                paletteItem.strokeWidth = 0
                            }
                            paletteItem.setCardBackgroundColor(state.palette[index].toColor())
                        }

                        binding.apply {
                            val color = state.palette[state.chosenPaletteItem]
                            sliderRed.value = color.r.toFloat()
                            sliderGreen.value = color.g.toFloat()
                            sliderBlue.value = color.b.toFloat()
                        }
                    }
                }
            }
        }
    }

    private fun getPaletteItemStrokeColor(): Int {
        return Color.WHITE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(panelNumber: Int): DrawingFragment {
            return DrawingFragment().apply {
                arguments = Bundle().apply {
                    putInt(PANEL_NUMBER_KEY, panelNumber)
                }
            }
        }
    }
}