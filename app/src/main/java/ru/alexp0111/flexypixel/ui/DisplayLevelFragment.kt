package ru.alexp0111.flexypixel.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
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
import soup.neumorphism.ShapeType
import java.util.Stack


class DisplayLevelFragment : Fragment() {

    private var _binding: FragmentDisplayLevelBinding? = null
    private val binding get() = _binding!!

    val dragListener = getDragListener(CardMode.FLAT)
    val holderDragListener = getDragListener(CardMode.RAISED)

    val images = mutableListOf<Int>()

    val displayHolder = Stack<Int>()
    val displayPosition = Array(3) { IntArray(3) }
    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
    }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentDisplayLevelBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        binding.card1.setOnDragListener(dragListener)
        binding.card2.setOnDragListener(dragListener)
        binding.card3.setOnDragListener(dragListener)
        binding.card4.setOnDragListener(dragListener)
        binding.card5.setOnDragListener(dragListener)
        binding.card6.setOnDragListener(dragListener)
        binding.card7.setOnDragListener(dragListener)
        binding.card8.setOnDragListener(dragListener)
        binding.card9.setOnDragListener(dragListener)
        binding.displayHolder.setOnDragListener(holderDragListener)

        getDisplayImages()
        spawnDisplays(9 , requireContext())

        fillUpMatrix(displayPosition)


    }

    private fun getDisplayImages() {
        images.add(R.drawable.c1)
        images.add(R.drawable.c2)
        images.add(R.drawable.c3)
        images.add(R.drawable.c4)
        images.add(R.drawable.c5)
        images.add(R.drawable.c6)
        images.add(R.drawable.c7)
        images.add(R.drawable.c8)
        images.add(R.drawable.c9)
    }


    fun getDragListener(cardMode: CardMode): View.OnDragListener {
        return View.OnDragListener { view , dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.visibility = View.VISIBLE
                    //Если в пазу уже есть карточки
                    if (cardMode == CardMode.FLAT && (view as FrameLayout).childCount > 1) {
                        return@OnDragListener false
                    }
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
                    Toast.makeText(requireContext() , dragData , Toast.LENGTH_SHORT).show()

                    view.invalidate()

                    val v = dragEvent.localState as soup.neumorphism.NeumorphImageView

                    val owner = v.parent as ViewGroup


                    val destination = view as FrameLayout
                    if (cardMode == CardMode.FLAT) {
                        v.setShadowElevation(600f)
                        v.setStrokeWidth(2f)

                        val destination_pos = getPosition(view)
                        val owner_pos = getPosition(owner)

                        //если вытаскиваем из нижней ячейки
                        if (owner_pos[0] == 3) {
                            val display_id = displayHolder.pop();
                            displayPosition[destination_pos[0]][destination_pos[1]] =
                                display_id
                        }
                        //если вытаскиваем из ячейки с позицией owner_pos
                        else {
                            displayPosition[destination_pos[0]][destination_pos[1]] =
                                displayPosition[owner_pos[0]][owner_pos[1]]
                            displayPosition[owner_pos[0]][owner_pos[1]] = 0
                        }
                    }
                    //если вытаскиваем из ячейки с позицией owner_pos и передаем в нижнюю ячейку
                    else if (cardMode == CardMode.RAISED) {
                        if (view.childCount == 1) v.setShadowElevation(9f)
                        val owner_pos = getPosition(owner)
                        val display_id = displayPosition[owner_pos[0]][owner_pos[1]]
                        displayPosition[owner_pos[0]][owner_pos[1]] = 0
                        displayHolder.push(display_id)
                        v.setStrokeWidth(0f)
                    }
                    printMatrix(displayPosition)
                    owner.removeView(v);
                    destination.addView(v)
                    v.visibility = View.VISIBLE
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

    @SuppressLint("RestrictedApi" , "UseCompatLoadingForDrawables")
    fun spawnDisplays(num: Int , context: Context) {
        for (i in 1..num) {
            val view = soup.neumorphism.NeumorphImageView(context)
            val layoutParams = FrameLayout.LayoutParams(
                dpToPx(125 , view.resources) ,
                dpToPx(125 , view.resources)
            )
            layoutParams.gravity = Gravity.CENTER
            view.layoutParams = layoutParams
            view.setStrokeColor(ColorStateList.valueOf(Color.WHITE))
            view.setPadding(dpToPx(30 , view.resources))
            if (i != 1) {
                view.setShadowElevation(600f)
            }
            displayHolder.push(i)
            view.setBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EEE4DC")))
            view.setShadowColorDark(Color.parseColor("#C5B9B0"))
            view.setShadowColorLight(Color.parseColor("#AEFFFFFF"))
            view.setShapeType(ShapeType.FLAT)
            view.setImageDrawable(context.getDrawable(images[i - 1]))
            view.scaleType = ImageView.ScaleType.FIT_CENTER

            view.setOnLongClickListener {
                val clipText = "ClipData Text $i"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText , mimeTypes , item)

                val dragShowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data , dragShowBuilder , it , 0)

                it.visibility = View.INVISIBLE
                true
            }
            binding.displayHolder.addView(view)

        }
    }

    private fun getPosition(view: View) = when (resources.getResourceName(view.id).toString().last()) {
        '1' -> arrayOf(0 , 0)
        '2' -> arrayOf(0 , 1)
        '3' -> arrayOf(0 , 2)
        '4' -> arrayOf(1 , 0)
        '5' -> arrayOf(1 , 1)
        '6' -> arrayOf(1 , 2)
        '7' -> arrayOf(2 , 0)
        '8' -> arrayOf(2 , 1)
        '9' -> arrayOf(2 , 2)
        else -> arrayOf(3 , 0)
    }

    fun dpToPx(dp: Int , resources: Resources): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    //Debug function
    fun fillUpMatrix(matrix: Array<IntArray>) {
        // Заполняем матрицу
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                // Пример заполнения матрицы случайными числами от 1 до 9
                matrix[i][j] = 0
            }
        }
    }

    //Debug function
    private fun printMatrix(matrix: Array<IntArray>) {
        var result = ""
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                result += matrix[i][j].toString() + " "
            }
            result += "\n"
        }
        Log.i("ViewID" , result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class CardMode() {
        FLAT ,
        RAISED
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

}