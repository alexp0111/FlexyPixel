package ru.alexp0111.flexypixel.ui.savedScheme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.databinding.FragmentSavedSchemesBinding
import ru.alexp0111.flexypixel.di.components.FragmentComponent
import ru.alexp0111.flexypixel.business.GlobalStateHandler
import javax.inject.Inject

class SavedSchemesFragment : Fragment() {

    private var _binding: FragmentSavedSchemesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var stateHolder: SavedSchemesViewModel

    private val savedSchemesAdapter by lazy {
        SavedSchemesAdapter{
            stateHolder.goToUpperLevelScreen(it.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectSelf()
        stateHolder.fillUpSavedSchemesList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSchemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.savedSchemesRv.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply{
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = savedSchemesAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    stateHolder.savedSchemesList.collect{
                        savedSchemesAdapter.updateSavedSchemesList(it)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalStateHandler.reset()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

}