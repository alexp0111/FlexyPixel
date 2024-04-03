package ru.alexp0111.flexypixel.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexp0111.flexypixel.bluetooth.MessageFrame
import ru.alexp0111.flexypixel.bluetooth.MessageHandler
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.databinding.FragmentTestBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

private const val TAG = "TestFragment"

class TestFragment : Fragment() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var messageHandler: MessageHandler

    private var pixel = 0
    private var testNumOfPanels = 0

    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestBinding.inflate(inflater)
        binding.apply {
            btnRemoveLastPixel.setOnClickListener {
                pixel--
                pixel = maxOf(pixel, 0)

                val pixOrder = pixel % 64
                val panOrder = pixel / 64

                etMovePanelNum.setText(panOrder.toString())
                etMovePixelNum.setText(pixOrder.toString())

                messageHandler.sendPixel(
                    panelOrder = panOrder,
                    pixelOrder = pixOrder,
                    rChannel = 0,
                    gChannel = 0,
                    bChannel = 0,
                )
            }

            btnMoveLastPixel.setOnClickListener {
                val r = (0..9).random()
                val g = (0..9).random()
                val b = (0..9).random()

                val pixOrder = pixel % 64
                val panOrder = pixel / 64

                etMovePanelNum.setText(panOrder.toString())
                etMovePixelNum.setText(pixOrder.toString())

                messageHandler.sendPixel(
                    panelOrder = panOrder,
                    pixelOrder = pixOrder,
                    rChannel = r,
                    gChannel = g,
                    bChannel = b,
                )

                pixel++
            }

            btnSendOnePixel.setOnClickListener {
                val r = etTstPixelR.text.toString().toInt()
                val g = etTstPixelG.text.toString().toInt()
                val b = etTstPixelB.text.toString().toInt()
                val pixOrder = etTstPixelOrder.text.toString().toInt()
                val panOrder = etTstPanelOrder.text.toString().toInt()

                messageHandler.sendPixel(
                    panelOrder = panOrder,
                    pixelOrder = pixOrder,
                    rChannel = r,
                    gChannel = g,
                    bChannel = b,
                )
            }

            btnSendFrames.setOnClickListener {
                val numberOfFrames = etNumberOfFrames.text.toString().toInt()
                val interfameDelay = etInterfameDelay.text.toString().toInt()
                messageHandler.sendFrames(
                    getTestFrames(numberOfFrames),
                    interfameDelay,
                )
            }

            btnSaveConfig.setOnClickListener {
                val numberOfPanels =
                    etTstConfiguration.text.toString().toInt().also { testNumOfPanels = it }
                val curConfig = Array(PanelConfiguration.MAX_SIZE) { "064" }
                for (i in numberOfPanels until PanelConfiguration.MAX_SIZE) {
                    curConfig[i] = "000"
                }
                messageHandler.updateConfiguration(curConfig)
            }
        }
        return binding.root
    }

    private fun getTestFrames(numberOfFrames: Int): List<MessageFrame> {
        val framesList = mutableListOf<MessageFrame>()
        val basicFrame = Array(testNumOfPanels) { Array(Panel.TYPE_64) { "000" } }
        for (i in 0 until numberOfFrames) {
            for (panelIndex in basicFrame.indices) {
                basicFrame[panelIndex][i] = "090"
            }
            val frame = MessageFrame(
                basicFrame.joinToString("") {
                    it.joinToString("")
                }
            )
            framesList.add(frame)
            Log.d(TAG, frame.asJson())
        }
        return framesList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    messageHandler.errors.collect {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                requireView(),
                                it,
                                Snackbar.LENGTH_SHORT,
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}