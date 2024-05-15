package ru.alexp0111.flexypixel.ui.drawing

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.graphics.toColor
import androidx.core.view.setMargins
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDrawingBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.start.device_pairing.Action
import ru.alexp0111.flexypixel.ui.start.device_pairing.UiState
import javax.inject.Inject
import kotlin.math.roundToInt


class DrawingFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var stateHolder: DrawingViewModel

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    private var paletteList = emptyList<MaterialCardView>()

    private val pixels = mutableListOf<MaterialCardView>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUpPaletteList()
        setPaletteOnClick()
        fillUpDrawingPanel()
        setUpSlidersOnChanged()
        subscribeUI()
    }

    private fun setUpSlidersOnChanged() {
        binding.apply {
            sliderRed.addOnChangeListener { slider, value, fromUser ->
                if (stateHolder.shouldIgnore)return@addOnChangeListener
                stateHolder.consumeAction(
                    DrawingAction.ChangePaletteItemColor(
                        DrawingColor(
                            r = value.toInt(),
                            g = sliderGreen.value.toInt(),
                            b = sliderBlue.value.toInt()
                        )
                    )
                )
            }
            sliderGreen.addOnChangeListener { slider, value, fromUser ->
                if (stateHolder.shouldIgnore)return@addOnChangeListener
                stateHolder.consumeAction(
                    DrawingAction.ChangePaletteItemColor(
                        DrawingColor(
                            r = sliderRed.value.toInt(),
                            g = value.toInt(),
                            b = sliderBlue.value.toInt()
                        )
                    )
                )
            }
            sliderBlue.addOnChangeListener { slider, value, fromUser ->
                if (stateHolder.shouldIgnore)return@addOnChangeListener
                stateHolder.consumeAction(
                    DrawingAction.ChangePaletteItemColor(
                        DrawingColor(
                            r = sliderRed.value.toInt(),
                            g = sliderGreen.value.toInt(),
                            b = value.toInt()
                        )
                    )
                )
            }


        }
    }

    private fun setPaletteOnClick() {
        for ((index, paletteItem) in paletteList.withIndex()) {
            paletteItem.setOnClickListener {
                stateHolder.consumeAction(DrawingAction.PickPaletteItem(index))
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

                val size = getResources().getDimension(R.dimen.pixel_card_size)
                pixel.radius = size / 5
                params.width = size.roundToInt()
                params.height = size.roundToInt()
                params.setMargins((size / 12).roundToInt())

                params.rowSpec = GridLayout.spec(i / 8)
                params.columnSpec = GridLayout.spec(i % 8)

                pixel.setCardBackgroundColor(Color.BLACK)
                pixel.strokeWidth = 0
                pixel.layoutParams = params
                pixel.setOnClickListener {
                    stateHolder.consumeAction(DrawingAction.RequestPixelColorUpdate(pixelPosition = i))
                    //Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
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
                    stateHolder.state.collect { state: DrawingUIState ->
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
                            stateHolder.shouldIgnore = true
                            sliderRed.value = color.r.toFloat()
                            sliderGreen.value = color.g.toFloat()
                            sliderBlue.value = color.b.toFloat()
                            stateHolder.shouldIgnore = false
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
}