package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState
import javax.inject.Inject

class UpperAbstractionLevelViewModel @Inject constructor(

) : MVIViewModel<UpperAbstractionLevelIntent, UpperAbstractionLevelUiState, Nothing>() {

    override fun setInitialState(): UpperAbstractionLevelUiState {
        return UpperAbstractionLevelUiState()
    }

    override fun handleIntent(intent: UpperAbstractionLevelIntent) =
        when (intent) {
            is UpperAbstractionLevelIntent.TitleChanged -> updateTitle(intent.newTitle)
            is UpperAbstractionLevelIntent.CardClicked -> {
                /* TODO: handle card click */
            }
        }

    private fun updateTitle(newTitle: String) {
        setState { uiState.value.copy(title = newTitle) }
    }
}