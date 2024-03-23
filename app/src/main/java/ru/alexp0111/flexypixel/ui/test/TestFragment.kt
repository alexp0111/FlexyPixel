package ru.alexp0111.flexypixel.ui.test

import android.os.Bundle
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
import ru.alexp0111.flexypixel.bluetooth.MessageHandler
import ru.alexp0111.flexypixel.databinding.FragmentTestBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

private const val TAG = "TestFragment"

class TestFragment : Fragment() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var messageHandler: MessageHandler

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

            btnSaveConfig.setOnClickListener {
                val numberOfPanels = etTstConfiguration.text.toString().toInt()
                messageHandler.updateConfiguration(numberOfPanels)
            }
        }
        return binding.root
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