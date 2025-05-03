package ru.alexp0111.flexypixel.ui.auth

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.auth.model.AuthEffect
import ru.alexp0111.flexypixel.ui.auth.model.AuthIntent
import ru.alexp0111.flexypixel.ui.auth.model.AuthUiState
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel
import ru.alexp0111.remote_repo.auth.repository.IAuthRepository
import ru.alexp0111.remote_repo.token.TokenManager
import javax.inject.Inject

internal class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val tokenManager: TokenManager,
    private val router: Router,
) : MVIViewModel<AuthIntent, AuthUiState, AuthEffect>() {

    override fun setInitialState(): AuthUiState = AuthUiState()

    override fun handleIntent(intent: AuthIntent) =
        when (intent) {
            is AuthIntent.OnEmailChanged -> setState { copy(email = intent.email) }
            is AuthIntent.OnPasswordChanged -> setState { copy(password = intent.password) }
            is AuthIntent.OnUsernameChanged -> setState { copy(username = intent.username) }
            AuthIntent.OnLoginClicked -> login()
            AuthIntent.OnRegisterClicked -> { /* todo */ }
            AuthIntent.OnResetPasswordClicked -> { /* todo */ }
        }

    private fun login() {
        val loginModel = ApiLoginModel(
            loginOrEmail = uiState.value.username,
            password = uiState.value.password,
        )
        launch {
            val token = authRepository.login(loginModel)
            token?.accessToken?.let {
                tokenManager.saveToken(TokenManager.ACCESS, it)
            }
            token?.refreshToken?.let {
                tokenManager.saveToken(TokenManager.REFRESH, it)
            }
            router.newRootScreen(Screens.StartScreen())
        }
    }
}