package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.alexp0111.flexypixel.databinding.FragmentUpperAbstractionLevelBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent


class UpperAbstractionLevelFragment : Fragment() {

    private var _binding: FragmentUpperAbstractionLevelBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpperAbstractionLevelBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListenersToGridElements()
    }

    private fun setOnClickListenersToGridElements() {
        binding.apply {
            val views = listOf(card1, card2, card3, card4, card5, card6, card7, card8, card9)
            for (view in views){
                view.setOnClickListener {
                    //There must be navigation logic
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }


}