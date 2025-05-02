package ru.alexp0111.flexypixel.ui.profile

import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.profile.model.ProfileIntent
import ru.alexp0111.flexypixel.ui.profile.model.ProfileUiState
import javax.inject.Inject

internal class ProfileViewModel @Inject constructor(

) : MVIViewModel<ProfileIntent, ProfileUiState, Nothing>() {

    override fun setInitialState(): ProfileUiState = ProfileUiState()

    override fun handleIntent(intent: ProfileIntent) =
        when (intent) {
            ProfileIntent.OnExitClicked -> TODO()
        }
}