package ru.alexp0111.flexypixel.ui.displayLevel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDisplayLevelBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.ui.EMPTY_CELL
import soup.neumorphism.NeumorphImageView
import soup.neumorphism.ShapeType
import java.util.Stack
import javax.inject.Inject

private const val SEGMENT_NUMBER_KEY = "SEGMENT_NUMBER_KEY"

class DisplayLevelFragment : Fragment() {

    @Inject
    lateinit var stateHolderFactory: DisplayLevelViewModelFactory

    private var stateHolder: DisplayLevelViewModel? = null

    private var _binding: FragmentDisplayLevelBinding? = null
    private val binding get() = _binding!!

    private val panelViewsList = mutableListOf<NeumorphImageView>()
    private val containerList = mutableListOf<FrameLayout>()

    private val matrixCellsDragListener = getDragListener(CardMode.FLAT)
    private val holderDragListener = getDragListener(CardMode.RAISED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
        stateHolder = stateHolderFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayLevelBinding.inflate(inflater, container, false)
        val segmentNumber = arguments?.getInt(SEGMENT_NUMBER_KEY) ?: 0
        stateHolder?.getPanelsConfiguration(segmentNumber)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpContainerList()
        setUpDragListeners()
        lifecycleScope.launch {
            stateHolder?.apply {
                val uiState = combine(
                    displayLocationInHolder,
                    displayLocationInMatrix,
                    bitmapMap
                ) { v1, v2, v3 ->
                    Triple(v1, v2, v3)
                }
                uiState.collect {
                    spawnDisplaysInHolder(it.first)
                    spawnDisplaysInMatrix(it.second)
                }
            }
        }

        //test logic
        binding.heading.setOnClickListener {
            Toast.makeText(requireContext(), stateHolder.toString(), Toast.LENGTH_LONG).show()
        }

    }

    private fun setUpContainerList() {
        binding.apply {
            containerList.add(card1)
            containerList.add(card2)
            containerList.add(card3)
            containerList.add(card4)
            containerList.add(card5)
            containerList.add(card6)
            containerList.add(card7)
            containerList.add(card8)
            containerList.add(card9)
        }
    }

    private fun setUpDragListeners() {
        for (container in containerList) {
            container.setOnDragListener(matrixCellsDragListener)
        }
        binding.displayHolder.setOnDragListener(holderDragListener)
    }


    private fun getDragListener(cardMode: CardMode): View.OnDragListener {
        return View.OnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.visibility = View.VISIBLE
                    dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    true
                }

                DragEvent.ACTION_DROP -> {
                    val item = dragEvent.clipData.getItemAt(0)
                    val dragData = item.text
                    view.invalidate()

                    val draggedView = dragEvent.localState as NeumorphImageView

                    val owner = draggedView.parent as FrameLayout
                    val destination = view as FrameLayout

                    val destinationPosition = getPosition(view)
                    val ownerPosition = getPosition(owner)

                    if (cardMode == CardMode.FLAT) {

                        draggedView.setShadowElevation(600f)
                        draggedView.setStrokeWidth(2f)


                        //если вытаскиваем из нижней ячейки
                        if (ownerPosition == DisplayLevelViewModel.HOLDER_POSITION) {
                            stateHolder?.fromHolderToMatrix(destinationPosition)
                        }
                        //если вытаскиваем из ячейки с позицией ownerPosition
                        else {
                            stateHolder?.fromMatrixToMatrix(ownerPosition, destinationPosition)
                        }
                    }
                    //если вытаскиваем из ячейки с позицией ownerPosition и передаем в нижнюю ячейку
                    else if (cardMode == CardMode.RAISED) {
                        if (view.childCount == 1) draggedView.setShadowElevation(9f)
                        draggedView.setStrokeWidth(0f)
                        stateHolder?.fromMatrixToHolder(ownerPosition)
                    }

                    owner.removeView(draggedView)
                    destination.addView(draggedView)

                    draggedView.visibility = View.VISIBLE
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    view.invalidate()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }


    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    private fun spawnDisplaysInMatrix(displayLocationInMatrix: MutableList<Int>) {
        for ((position, panelNumber) in displayLocationInMatrix.withIndex()) {

            if (panelNumber == EMPTY_CELL) continue

            val panelView = getPanelView(position, panelNumber, SpawnMode.MATRIX)

            panelViewsList.add(panelView)

            containerList[position].addView(panelView)
        }

    }

    private fun spawnDisplaysInHolder(displayLocationInHolder: Stack<Int>) {
        for ((position, panelNumber) in displayLocationInHolder.withIndex()) {

            val panelView = getPanelView(position, panelNumber, SpawnMode.HOLDER)

            panelViewsList.add(panelView)

            binding.displayHolder.addView(panelView)
        }
    }

    enum class SpawnMode {
        MATRIX,
        HOLDER
    }

    @SuppressLint("RestrictedApi")
    private fun getPanelView(
        position: Int,
        panelNumber: Int,
        spawnMode: SpawnMode
    ): NeumorphImageView {
        val panelView = NeumorphImageView(requireContext())

        val layoutParams = FrameLayout.LayoutParams(
            dpToPx(125, panelView.resources),
            dpToPx(125, panelView.resources)
        )

        layoutParams.gravity = Gravity.CENTER
        panelView.layoutParams = layoutParams
        panelView.setStrokeColor(ColorStateList.valueOf(Color.WHITE))
        panelView.setPadding(dpToPx(30, panelView.resources))

        if (position != 0 || spawnMode == SpawnMode.MATRIX) {
            panelView.setShadowElevation(600f)
        }

        if (spawnMode == SpawnMode.MATRIX) {
            panelView.setStrokeWidth(2f)
        }

        panelView.setBackgroundColor(resources.getColor(R.color.beige))
        panelView.setShadowColorDark(resources.getColor(R.color.darker_beige))
        panelView.setShadowColorLight(resources.getColor(R.color.white_shadow))
        panelView.setShapeType(ShapeType.FLAT)

        val bitmap: Bitmap? = stateHolder?.bitmapMap?.value?.getOrElse(panelNumber) { null }

        if (bitmap != null) {
            panelView.setImageBitmap(bitmap)
        } else {
            panelView.setBackgroundColor(Color.BLACK)
        }

        panelView.scaleType = ImageView.ScaleType.FIT_CENTER

        panelView.setOnLongClickListener {
            val clipText = "ClipData Text $panelNumber"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        panelView.setOnClickListener {
            stateHolder?.goToDrawingFragment(panelNumber)
        }
        return panelView
    }


    private fun getPosition(view: View) =
        when (resources.getResourceName(view.id).toString().last()) {
            '1' -> 0
            '2' -> 1
            '3' -> 2
            '4' -> 3
            '5' -> 4
            '6' -> 5
            '7' -> 6
            '8' -> 7
            '9' -> 8
            else -> -1
        }

    fun dpToPx(dp: Int, resources: Resources): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        val segmentNumber = arguments?.getInt(SEGMENT_NUMBER_KEY) ?: 0
        stateHolder?.sendPanelsConfiguration(segmentNumber)
        panelViewsList.clear()
        containerList.clear()
        super.onStop()
    }

    enum class CardMode {
        FLAT,
        RAISED
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