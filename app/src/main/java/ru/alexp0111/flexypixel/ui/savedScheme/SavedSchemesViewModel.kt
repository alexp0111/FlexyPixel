package ru.alexp0111.flexypixel.ui.savedScheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.database.schemes.SavedSchemeRepository
import ru.alexp0111.flexypixel.database.schemes.data.UserSavedScheme
import ru.alexp0111.flexypixel.navigation.Screens
import javax.inject.Inject

class SavedSchemesViewModel @Inject constructor(
    private val databaseRepository: SavedSchemeRepository,
    private val router: Router,
) : ViewModel() {

    var savedSchemesList = MutableStateFlow(mutableListOf<UserSavedScheme>())

    fun fillUpSavedSchemesList() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getAllSchemes().collect { incomingList ->
                savedSchemesList.update { incomingList.toMutableList() }
            }
        }
    }

    fun goToUpperLevelScreen(schemeId: Int) {
        router.navigateTo(Screens.UpperAbstractionLevelScreen(schemeId))
    }
}