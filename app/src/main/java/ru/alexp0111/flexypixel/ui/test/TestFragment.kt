package ru.alexp0111.flexypixel.ui.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexp0111.flexypixel.bluetooth.MessageFrame
import ru.alexp0111.flexypixel.bluetooth.MessageHandler
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.databinding.FragmentTestBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.media.MediaFilesProcessor
import javax.inject.Inject


private const val TAG = "TestFragment"

class TestFragment : Fragment() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var messageHandler: MessageHandler

    @Inject
    lateinit var mediaFilesProcessor: MediaFilesProcessor

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
                val interframeDelay = etInterfameDelay.text.toString().toInt()
                messageHandler.sendFrames(
                    getTestFrames(numberOfFrames),
                    interframeDelay,
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

            btnLoadMedia.setOnClickListener {
                requestDataFromSystemProvider()
            }
        }
        return binding.root
    }

    private val pickMediaFromDevicesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val uri = result.data?.data
        uri?.let {
            Log.d(TAG, it.toString())
            Glide.with(requireContext()).load(it).into(
                binding.ivLoadedData
            )
            val metadataList = listOf(
                PanelMetaData(
                    order = 0,
                    type = PanelMetaData.TYPE_64,
                    absoluteX = 0,
                    absoluteY = 0,
                    rotation = 0,
                )
            )
            mediaFilesProcessor.handleIncomingMedia(it, metadataList)
        }
    }

    private fun requestDataFromSystemProvider() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "gif/*"))
            }
            pickMediaFromDevicesLauncher.launch(intent)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }


    private fun getTestFrames(numberOfFrames: Int): List<MessageFrame> {
        val framesList = mutableListOf<MessageFrame>()
        val basicFrame = Array(testNumOfPanels) { Array(PanelMetaData.TYPE_64) { "000" } }
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

                launch {
                    mediaFilesProcessor.bitmaps.collect { bitmapList ->
                        bitmapList.forEachIndexed { index, bitmap ->
                            val bitmapAsString = BitmapProcessor.convertBitmapToPixelStringMatrix(bitmap)
                            // BitmapProcessor.applyBitmapToFrame()
                            // messageHandler.sendFrames(listOf(MessageFrame(bitmapAsString)), 1000)
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