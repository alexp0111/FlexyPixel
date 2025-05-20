package ru.alexp0111.flexypixel.ui.savedScheme

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.saved_scheme.ISavedSchemeInteractor
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.savedScheme.converter.ISavedSchemeConvertor
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeEffect
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeIntent
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiState
import javax.inject.Inject

internal class SavedSchemesViewModel @Inject constructor(
    private val savedSchemeInteractor: ISavedSchemeInteractor,
    private val savedSchemeConvertor: ISavedSchemeConvertor,
    private val router: Router,
) : MVIViewModel<SavedSchemeIntent, SavedSchemeUiState, SavedSchemeEffect>() {

    init {
        launch {
            setState { copy(isLoading = true) }
            val schemes = savedSchemeInteractor.getAllSchemes().map(savedSchemeConvertor::convertToUiModel)
            setState { copy(isLoading = false, schemeItems = schemes) }
        }
    }

    override fun setInitialState(): SavedSchemeUiState = SavedSchemeUiState()

    override fun handleIntent(intent: SavedSchemeIntent) =
        when(intent) {
            is SavedSchemeIntent.SchemeItemClicked -> goToUpperLevelScreen(intent.uuid)
        }

    private fun goToUpperLevelScreen(schemeId: String) {
        router.navigateTo(Screens.UpperAbstractionLevelScreen(schemeId))
    }
}