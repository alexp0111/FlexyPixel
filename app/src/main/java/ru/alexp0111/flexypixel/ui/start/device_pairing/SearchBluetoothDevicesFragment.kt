package ru.alexp0111.flexypixel.ui.start.device_pairing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.databinding.FragmentSearchBluetoothDevicesBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import javax.inject.Inject

private const val TAG = "SearchBluetoothDevicesFragment"

class SearchBluetoothDevicesFragment : Fragment() {

    @Inject
    lateinit var stateHolder: SearchBluetoothDevicesViewModel

    private val availableDevicesAdapter by lazy {
        SearchBluetoothDevicesAdapter {
            if (stateHolder.state.value.connectedDevice == null) {
                stateHolder.consumeAction(Action.ConnectBluetoothDevice(it))
            } else {
                Snackbar.make(
                    requireView() ,
                    "You have been already connected!" ,
                    Snackbar.LENGTH_SHORT ,
                ).show()
            }
        }
    }

    private var _binding: FragmentSearchBluetoothDevicesBinding? = null
    private val binding: FragmentSearchBluetoothDevicesBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle? ,
    ): View {
        _binding = FragmentSearchBluetoothDevicesBinding.inflate(layoutInflater)
        binding.apply {
            rvAvailableDevices.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            rvAvailableDevices.adapter = availableDevicesAdapter

            srlAvailableDevices.setOnRefreshListener {
                stateHolder.consumeAction(Action.StopLoadingBluetoothDevices)
                stateHolder.consumeAction(Action.LoadAvailableDevices)
                srlAvailableDevices.isRefreshing = false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        subscribeUI()
       // val testDeviceList = listOf(Pair("Device 1",BluetoothDeviceState("Device 1")),Pair("Device 2",BluetoothDeviceState("Device 2", isConnected = true)),Pair("Device 3",BluetoothDeviceState("Device 3"))).toMutableList()
      //  availableDevicesAdapter.list = testDeviceList

    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    stateHolder.state.collect { state: UiState ->
                        availableDevicesAdapter.apply {
                            setConnectedDevice(state.connectedDevice)
                            updateList(state.foundDevices)
                        }

                        if (state.hasError) {
                            showError()
                            stateHolder.consumeAction(Action.NotifyErrorShown)
                        }
                    }
                }
            }
        }
    }

    private fun showError() {
        Snackbar.make(requireView() , "Connection failed" , Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        stateHolder.consumeAction(Action.LoadAvailableDevices)
    }

    override fun onDestroy() {
        super.onDestroy()
        stateHolder.consumeAction(Action.StopLoadingBluetoothDevices)
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}