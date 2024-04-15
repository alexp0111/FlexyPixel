package ru.alexp0111.flexypixel.ui.util

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.alexp0111.flexypixel.databinding.FragmentBluetoothResolverBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.util.PermissionResolver
import javax.inject.Inject

/*
* TODO: We should show info dialog before asking for turning on GPS
* */

class BluetoothResolverFragment : Fragment() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    @Inject
    lateinit var permissionResolver: PermissionResolver

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (!permissionResolver.isBluetoothOn()) {
            requireActivity().finish()
        } else {
            resolveCurrentState()
        }
    }

    private val enableGPSLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (!permissionResolver.isGPSOn()) {
            requireActivity().finish()
        } else {
            resolveCurrentState()
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            requireActivity().finish()
        } else {
            resolveCurrentState()
        }
    }

    private var _binding: FragmentBluetoothResolverBinding? = null
    private val binding: FragmentBluetoothResolverBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        resolveCurrentState()
        super.onCreate(savedInstanceState)
    }

    private fun resolveCurrentState() {
        val isBluetoothOn = permissionResolver.isBluetoothOn()
        val isGPSOn = permissionResolver.isGPSOn()
        val isPermissionsProvided = permissionResolver.isBluetoothPermissionsGranted()

        when {
            !isPermissionsProvided -> permissionLauncher.launch(permissionResolver.getNecessaryPermissions())
            !isBluetoothOn -> enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            !isGPSOn -> enableGPSLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            else -> router.exit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBluetoothResolverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}