package ru.alexp0111.flexypixel.ui.profile

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.profile.IProfileInteractor
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.profile.model.ProfileIntent
import ru.alexp0111.flexypixel.ui.profile.model.ProfileUiState
import ru.alexp0111.remote_repo.token.TokenManager
import javax.inject.Inject

internal class ProfileViewModel @Inject constructor(
    private val profileInteractor: IProfileInteractor,
    private val tokenManager: TokenManager,
    private val router: Router,
) : MVIViewModel<ProfileIntent, ProfileUiState, Nothing>() {

    init {
        launch {
            profileInteractor.getUser().let { user ->
                setState {
                    copy(
                        username = user.displayName,
                        email = user.email,
                        registerDate = user.registerDate,
                        profileUri = user.profileUri,
                    )
                }
            }
        }
    }

    override fun setInitialState(): ProfileUiState = ProfileUiState()

    override fun handleIntent(intent: ProfileIntent) =
        when (intent) {
            ProfileIntent.OnExitClicked -> logout()
        }

    private fun logout() {
        tokenManager.deleteToken(TokenManager.ACCESS)
        tokenManager.deleteToken(TokenManager.REFRESH)
        router.newRootScreen(Screens.StartScreen())
    }
}