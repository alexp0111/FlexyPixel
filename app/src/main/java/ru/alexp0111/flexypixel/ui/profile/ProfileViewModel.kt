package ru.alexp0111.flexypixel.ui.profile

import android.net.Uri
import androidx.core.net.toUri
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.profile.IProfileInteractor
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.profile.model.ProfileEffect
import ru.alexp0111.flexypixel.ui.profile.model.ProfileIntent
import ru.alexp0111.flexypixel.ui.profile.model.ProfileUiState
import ru.alexp0111.remote_repo.token.TokenManager
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal class ProfileViewModel @Inject constructor(
    private val profileInteractor: IProfileInteractor,
    private val tokenManager: TokenManager,
    private val router: Router,
) : MVIViewModel<ProfileIntent, ProfileUiState, ProfileEffect>() {

    init {
        launch {
            setState { copy(isLoading = true) }
            profileInteractor.getUser().let { user ->
                setState {
                    copy(
                        isLoading = false,
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
            is ProfileIntent.OnProfilePictureChosen -> setProfilePicture(intent.uri)
        }

    private fun logout() {
        tokenManager.deleteToken(TokenManager.ACCESS)
        tokenManager.deleteToken(TokenManager.REFRESH)
        router.newRootScreen(Screens.StartScreen())
    }

    private fun setProfilePicture(uri: Uri) {
        launch {
            setState { copy(isLoading = true) }
            val avatar = profileInteractor.loadProfilePicture(uri)?.avatarUrl
            avatar?.let { setState { copy(isLoading = false, profileUri = it.toUri()) } }
        }
    }

    override fun errorHandler(context: CoroutineContext, throwable: Throwable) {
        super.errorHandler(context, throwable)
        setState { copy(isLoading = false) }
    }

    override fun httpErrorHandler(errorMessage: String, errorCode: Int) {
        super.httpErrorHandler(errorMessage, errorCode)
        setEffect { ProfileEffect.ShowSnackBar(errorMessage) }
    }
}