package ru.alexp0111.flexypixel.ui.util

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
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
        if (!permissionResolver.isBluetoothAvailable()) {
            requireActivity().finish()
            return@registerForActivityResult
        }
        router.exit()
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { _ ->
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            requireActivity().finish()
            return@registerForActivityResult
        }
        if (!permissionResolver.isBluetoothAvailable()) {
            enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } else {
            router.exit()
        }
    }

    private var _binding: FragmentBluetoothResolverBinding? = null
    private val binding: FragmentBluetoothResolverBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        if (!permissionResolver.isBluetoothPermissionsGranted()) {
            val permissions = permissionResolver.getNecessaryPermissions()
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            if (!permissionResolver.isBluetoothAvailable()) {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            } else {
                router.exit()
            }
        }
        super.onCreate(savedInstanceState)
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