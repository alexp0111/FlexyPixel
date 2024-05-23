package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentUpperAbstractionLevelBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.menu.NEW_SCHEME_CODE
import soup.neumorphism.NeumorphCardView
import javax.inject.Inject

private const val SCHEME_ID_KEY = "SCHEME_ID_KEY"

class UpperAbstractionLevelFragment : Fragment() {

    @Inject
    lateinit var stateHolderFactory: UpperAbstractionLevelViewModelFactory

    private var stateHolder: UpperAbstractionLevelViewModel? = null

    private var _binding: FragmentUpperAbstractionLevelBinding? = null
    private val binding get() = _binding!!

    private var segmentsList = listOf<NeumorphCardView>()
    private var imageViewsList = listOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        var schemeId = arguments?.getInt(SCHEME_ID_KEY, NEW_SCHEME_CODE)
        if (schemeId == NEW_SCHEME_CODE) schemeId = null
        stateHolder = stateHolderFactory.create(schemeId)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        stateHolder?.getSegmentsImages()
        stateHolder?.getTitle()
        _binding = FragmentUpperAbstractionLevelBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUpSegmentsAndImagesList()
        setOnClickListenersToGridElements()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    stateHolder?.segmentImagesBitmapList?.collect {
                        setSegmentsImages(it)
                    }
                }
                launch {
                    stateHolder?.title?.collect {
                        if (it != null) {
                            binding.heading.setText(it)
                        }
                    }
                }
            }
        }
        binding.apply {
            saveBtn.setOnClickListener {
                if (heading.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Please, set title", Toast.LENGTH_SHORT).show()
                } else {
                    var schemeId = arguments?.getInt(SCHEME_ID_KEY, NEW_SCHEME_CODE)
                    if (schemeId == NEW_SCHEME_CODE) schemeId = null
                    stateHolder?.save(heading.text.toString())
                }
            }
        }
    }

    private fun fillUpSegmentsAndImagesList() {
        binding.apply {
            segmentsList = listOf(card1, card2, card3, card4, card5, card6, card7, card8, card9)
            imageViewsList =
                listOf(image1, image2, image3, image4, image5, image6, image7, image8, image9)
        }
    }

    private fun setSegmentsImages(images: List<Bitmap?>) {
        for ((index, image) in images.withIndex()) {
            if (image == null) {
                segmentsList[index].setBackgroundColor(resources.getColor(R.color.beige))
                continue
            }
            imageViewsList[index].setImageBitmap(image)
        }
    }

    private fun setOnClickListenersToGridElements() {
        binding.apply {
            segmentsList.forEachIndexed { segmentNumber, view ->
                view.setOnClickListener {
                    stateHolder?.goToDisplayLevel(segmentNumber)
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

    companion object {
        fun newInstance(schemeId: Int): UpperAbstractionLevelFragment {
            return UpperAbstractionLevelFragment().apply {
                arguments = Bundle().apply {
                    putInt(SCHEME_ID_KEY, schemeId)
                }
            }
        }
    }

}