package ru.alexp0111.flexypixel.ui.displayLevel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.setPadding
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.databinding.FragmentDisplayLevelBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import soup.neumorphism.NeumorphImageView
import soup.neumorphism.ShapeType
import javax.inject.Inject


class DisplayLevelFragment : Fragment() {
    @Inject
    lateinit var stateHolder: DisplayLevelViewModel

    private var _binding: FragmentDisplayLevelBinding? = null
    private val binding get() = _binding!!

    val panelViewsList = mutableListOf<NeumorphImageView>()
    val containerList = mutableListOf<FrameLayout>()

    val dragListener = getDragListener(CardMode.FLAT)
    val holderDragListener = getDragListener(CardMode.RAISED)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentDisplayLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpContainerList()
        setUpDragListeners()
        TEST_putDisplayImages()
        spawnDisplays()
        binding.heading.setOnClickListener {
            Toast.makeText(requireContext(),stateHolder.toString(),Toast.LENGTH_LONG).show()
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

    //test function
    private fun TEST_putDisplayImages() {
        stateHolder.apply {
            bitmapMap[1] = BitmapFactory.decodeResource(resources, R.drawable.c1)
            bitmapMap[2] = BitmapFactory.decodeResource(resources, R.drawable.c2)
            bitmapMap[3] = BitmapFactory.decodeResource(resources, R.drawable.c3)
            bitmapMap[4] = BitmapFactory.decodeResource(resources, R.drawable.c4)
            //    bitmapMap[5] = BitmapFactory.decodeResource(resources, R.drawable.c5)
            bitmapMap[6] = BitmapFactory.decodeResource(resources, R.drawable.c6)
            bitmapMap[7] = BitmapFactory.decodeResource(resources, R.drawable.c7)
            bitmapMap[8] = BitmapFactory.decodeResource(resources, R.drawable.c8)
            bitmapMap[9] = BitmapFactory.decodeResource(resources, R.drawable.c9)
        }
    }

    private fun setUpDragListeners() {
        for (container in containerList) {
            container.setOnDragListener(getDragListener(CardMode.FLAT))
        }
        binding.displayHolder.setOnDragListener(holderDragListener)
    }


    fun getDragListener(cardMode: CardMode): View.OnDragListener {
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
                    if (destination.childCount>1 && getPosition(destination)!=-1){
                        return@OnDragListener false
                    }
                    val destination_pos = getPosition(view)
                    val owner_pos = getPosition(owner)

                    if (cardMode == CardMode.FLAT) {
                        draggedView.setShadowElevation(600f)
                        draggedView.setStrokeWidth(2f)



                        //если вытаскиваем из нижней ячейки
                        if (owner_pos == DisplayLevelViewModel.HOLDER_POSITION) {
                            stateHolder.fromHolderToMatrix(destination_pos)
                        }
                        //если вытаскиваем из ячейки с позицией owner_pos
                        else {
                            stateHolder.fromMatrixToMatrix(owner_pos,destination_pos)
                        }
                    }
                    //если вытаскиваем из ячейки с позицией owner_pos и передаем в нижнюю ячейку
                    else if (cardMode == CardMode.RAISED) {
                        if (view.childCount == 1) draggedView.setShadowElevation(9f)
                        draggedView.setStrokeWidth(0f)

                        stateHolder.fromMatrixToHolder(owner_pos)
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
    fun spawnDisplays() {
        for ((index, i) in stateHolder.displayLocationInHolder.withIndex()) {

            val view = NeumorphImageView(requireContext())

            val layoutParams = FrameLayout.LayoutParams(
                dpToPx(125, view.resources),
                dpToPx(125, view.resources)
            )

            layoutParams.gravity = Gravity.CENTER
            view.layoutParams = layoutParams
            view.setStrokeColor(ColorStateList.valueOf(Color.WHITE))
            view.setPadding(dpToPx(30, view.resources))



            if (index != 0) {
                view.setShadowElevation(600f)
            }

            view.setBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EEE4DC")))
            view.setShadowColorDark(Color.parseColor("#C5B9B0"))
            view.setShadowColorLight(Color.parseColor("#AEFFFFFF"))
            view.setShapeType(ShapeType.FLAT)


            val bitmap: Bitmap? = stateHolder.bitmapMap.getOrElse(i) {
                null
            }
            if (bitmap != null) {
                view.setImageBitmap(bitmap)
            } else {
                view.setBackgroundColor(Color.BLACK)
            }

            view.scaleType = ImageView.ScaleType.FIT_CENTER

            view.setOnLongClickListener {
                val clipText = "ClipData Text $i"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText, mimeTypes, item)

                val dragShowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data, dragShowBuilder, it, 0)

                it.visibility = View.INVISIBLE
                true
            }
            panelViewsList.add(view)

            binding.displayHolder.addView(view)
        }

        for ((position, i) in stateHolder.displayLocationInMatrix.withIndex()) {

            if (i == DisplayLevelViewModel.EMPTY_CELL) continue

            val view = NeumorphImageView(requireContext())

            val layoutParams = FrameLayout.LayoutParams(
                dpToPx(125, view.resources),
                dpToPx(125, view.resources)
            )

            layoutParams.gravity = Gravity.CENTER
            view.layoutParams = layoutParams
            view.setStrokeColor(ColorStateList.valueOf(Color.WHITE))
            view.setStrokeWidth(2f)
            view.setPadding(dpToPx(30, view.resources))


            view.setShadowElevation(600f)


            view.setBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EEE4DC")))
            view.setShadowColorDark(Color.parseColor("#C5B9B0"))
            view.setShadowColorLight(Color.parseColor("#AEFFFFFF"))
            view.setShapeType(ShapeType.FLAT)


            val bitmap: Bitmap? = stateHolder.bitmapMap.getOrElse(i) {
                null
            }
            if (bitmap != null) {
                view.setImageBitmap(bitmap)
            } else {
                view.setBackgroundColor(Color.BLACK)
            }

            view.scaleType = ImageView.ScaleType.FIT_CENTER

            view.setOnLongClickListener {
                val clipText = "ClipData Text $i"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText, mimeTypes, item)

                val dragShowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data, dragShowBuilder, it, 0)

                it.visibility = View.INVISIBLE
                true
            }
            panelViewsList.add(view)

            containerList[position].addView(view)
        }

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

    enum class CardMode() {
        FLAT,
        RAISED
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

}