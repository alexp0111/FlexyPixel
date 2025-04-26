package ru.alexp0111.flexypixel.ui.auth

import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.auth.model.AuthEffect
import ru.alexp0111.flexypixel.ui.auth.model.AuthIntent
import ru.alexp0111.flexypixel.ui.auth.model.AuthUiState
import javax.inject.Inject

internal class AuthViewModel @Inject constructor(

) : MVIViewModel<AuthIntent, AuthUiState, AuthEffect>() {

    override fun setInitialState(): AuthUiState = AuthUiState()

    override fun handleIntent(intent: AuthIntent) =
        when(intent) {
            is AuthIntent.OnEmailChanged -> TODO()
            is AuthIntent.OnPasswordChanged -> TODO()
            is AuthIntent.OnUsernameChanged -> TODO()
            AuthIntent.OnLoginClicked -> TODO()
            AuthIntent.OnRegisterClicked -> TODO()
            AuthIntent.OnResetPasswordClicked -> TODO()
        }
}