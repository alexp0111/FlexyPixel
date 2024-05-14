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
import com.google.android.material.card.MaterialCardView
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDrawingBinding
import kotlin.math.roundToInt


class DrawingFragment : Fragment() {

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    private var paletteList = emptyList<MaterialCardView>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUpPaletteList()
        setUpColors()
        setPaletteOnClick()
        fillUpDrawingPanel()
    }

    private fun setPaletteOnClick() {
        for (paletteItem in paletteList) {
            paletteItem.setOnClickListener {

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

                pixel.setCardBackgroundColor(getPixelColor(i))
                pixel.strokeWidth = 0
                pixel.layoutParams = params
                pixel.setOnClickListener {
                    Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
                    pixelOnClick(i)
                }
                this.addView(pixel)
            }
        }
    }

    private fun pixelOnClick(pixelPosition: Int) {
        //Logic
    }


    private fun getPixelColor(pixelPosition: Int): Int {
        return Color.RED
    }

    private fun setUpColors() {
        val colors = getPalette()
        var i = 0
        for (item in paletteList) {
            item.setCardBackgroundColor(colors[i])
            i++
        }
    }

    private fun getPalette(): List<Int> {
        return listOf<Int>(
            Color.BLACK,
            Color.WHITE,
            Color.BLUE,
            Color.CYAN,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.GREEN,
            Color.RED,
            Color.YELLOW,
            Color.DKGRAY,
            Color.YELLOW,
            Color.BLACK,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}