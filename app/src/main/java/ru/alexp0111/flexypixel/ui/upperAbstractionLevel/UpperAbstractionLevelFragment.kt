package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.databinding.FragmentUpperAbstractionLevelBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import soup.neumorphism.NeumorphImageView
import javax.inject.Inject


class UpperAbstractionLevelFragment : Fragment() {

    @Inject
    lateinit var stateHolderFactory: UpperAbstractionLevelViewModelFactory

    private var stateHolder: UpperAbstractionLevelViewModel? = null

    private var _binding: FragmentUpperAbstractionLevelBinding? = null
    private val binding get() = _binding!!

    private var segmentsList = listOf<NeumorphImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        val schemeId = null // TODO: Get from arguments
        stateHolder = stateHolderFactory.create(schemeId)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        stateHolder?.getSegmentsImages()
        _binding = FragmentUpperAbstractionLevelBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUpSegmentsList()
        setOnClickListenersToGridElements()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateHolder?.segmentImagesBitmapList?.collect {
                    setSegmentsImages(it)
                }
            }
        }

    }

    private fun fillUpSegmentsList() {
        binding.apply {
            segmentsList = listOf(card1, card2, card3, card4, card5, card6, card7, card8, card9)
        }
    }

    private fun setSegmentsImages(images: List<Bitmap?>) {
        for ((index, image) in images.withIndex()) {
            if (image == null) {
                segmentsList[index].setBackgroundColor(Color.BLACK)
                continue
            }
            segmentsList[index].setImageBitmap(image)
        }
    }

    private fun setOnClickListenersToGridElements() {
        binding.apply {
            for (view in segmentsList) {
                view.setOnClickListener {
                    Toast.makeText(requireContext(), "Tapped", Toast.LENGTH_SHORT).show()
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