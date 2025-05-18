package ru.alexp0111.flexypixel.ui.auth

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.auth.model.AuthEffect
import ru.alexp0111.flexypixel.ui.auth.model.AuthIntent
import ru.alexp0111.flexypixel.ui.auth.model.AuthScreenType
import ru.alexp0111.flexypixel.ui.auth.model.AuthUiState
import ru.alexp0111.remote_repo.auth.model.ApiLoginModel
import ru.alexp0111.remote_repo.auth.model.ApiRegisterModel
import ru.alexp0111.remote_repo.auth.repository.IAuthRepository
import ru.alexp0111.remote_repo.token.TokenManager
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val tokenManager: TokenManager,
    private val router: Router,
) : MVIViewModel<AuthIntent, AuthUiState, AuthEffect>() {

    override fun setInitialState(): AuthUiState = AuthUiState()

    override fun handleIntent(intent: AuthIntent) =
        when (intent) {
            is AuthIntent.OnLoginChanged -> setState { copy(login = intent.login) }
            is AuthIntent.OnPasswordChanged -> setState { copy(password = intent.password) }
            is AuthIntent.OnDisplayNameChanged -> setState { copy(displayName = intent.displayName) }
            is AuthIntent.OnEmailChanged -> setState { copy(email = intent.email) }
            AuthIntent.OnLoginClicked -> login()
            AuthIntent.OnRegisterClicked -> register()
            AuthIntent.OnResetPasswordClicked -> { /* todo */ }
            AuthIntent.OnGoToRegisterClicked -> setState { copy(type = AuthScreenType.REGISTER) }
            AuthIntent.OnGoToLoginClicked -> setState { copy(type = AuthScreenType.LOGIN) }
        }

    private fun login() {
        passLoginChecks()?.let { checks ->
            setEffect { AuthEffect.ShowSnackBar(checks) }
            return
        }

        val loginModel = ApiLoginModel(
            loginOrEmail = uiState.value.login,
            password = uiState.value.password,
        )
        launch {
            setState { copy(isLoading = true) }
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

    private fun register() {
        passRegisterChecks()?.let { checks ->
            setEffect { AuthEffect.ShowSnackBar(checks) }
            return
        }

        val registerModel = ApiRegisterModel(
            email = uiState.value.email,
            displayName = uiState.value.displayName,
            login = uiState.value.login,
            password = uiState.value.password,
        )
        launch {
            setState { copy(isLoading = true) }
            authRepository.register(registerModel)
            setEffect { AuthEffect.ShowSnackBar("Registered successfully") }
            setState {
                copy(
                    isLoading = false,
                    type = AuthScreenType.LOGIN,
                    password = "",
                )
            }
        }
    }

    private fun passLoginChecks(): String? {
        val checksText = buildString {
            if (uiState.value.login.isEmpty()) append("Login should not be empty!\n")
            if (uiState.value.password.isEmpty()) append("Password should not be empty!\n")
        }.trimIndent()

        return checksText.takeIf { it.isNotEmpty() }
    }

    private fun passRegisterChecks(): String? {
        val checksText = buildString {
            if (uiState.value.login.isEmpty()) append("Login should not be empty!\n")
            if (uiState.value.email.isEmpty()) append("Email should not be empty!\n")
            if (!isValidEmail(uiState.value.email)) append("Email invalid!\n")
            if (uiState.value.displayName.isEmpty()) append("Display name should not be empty!\n")
            if (uiState.value.password.isEmpty()) append("Password should not be empty!\n")
        }.trimIndent()

        return checksText.takeIf { it.isNotEmpty() }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})([A-Za-z0-9._-]{1,})(\\.{1})([A-Za-z]{2,6})\$".toRegex()
        return email.matches(emailRegex)
    }

    override fun errorHandler(context: CoroutineContext, throwable: Throwable) {
        super.errorHandler(context, throwable)
        setState { copy(isLoading = false) }
    }

    override fun httpErrorHandler(errorMessage: String, errorCode: Int) {
        super.httpErrorHandler(errorMessage, errorCode)
        setEffect { AuthEffect.ShowSnackBar(errorMessage) }
    }
}