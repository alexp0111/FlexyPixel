package ru.alexp0111.flexypixel.ui.savedScheme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import javax.inject.Inject

class SavedSchemesViewModel @Inject constructor() : ViewModel() {
    var savedSchemesList = MutableStateFlow(mutableListOf<SavedSchemeItem>())

    fun getSavedSchemesList() {
        //TODO update savedSchemesList from DB
    }

    //TEST FUNCTION
    fun fillUpSavedSchemesList() {
        savedSchemesList.update {
            for (i in 1..20) {
                it.add(SavedSchemeItem(name = "Template #$i"))
            }
            it
        }
    }
}