package ru.alexp0111.flexypixel.ui.drawing

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import com.google.android.material.card.MaterialCardView
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDrawingBinding


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

    private fun setUpColors() {

        val colors = listOf<Int>(
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
        var i = 0
        for (item in paletteList) {
            item.setCardBackgroundColor(colors[i])
            i++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}