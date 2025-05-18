package ru.alexp0111.flexypixel.ui.auth

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.alexp0111.core_ui.R
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core_ui.common.NeoSlotRounded
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumEditTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.flexypixel.ui.auth.model.AuthEffect
import ru.alexp0111.flexypixel.ui.auth.model.AuthIntent
import ru.alexp0111.flexypixel.ui.auth.model.AuthScreenType
import ru.alexp0111.flexypixel.ui.auth.model.AuthUiState
import ru.alexp0111.core_ui.R as coreDesignR

@Composable
internal fun AuthScreen(viewModel: AuthViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is AuthEffect.ShowSnackBar -> snackBarHostState.showSnackbar(it.message)
            }
        }
    }

    AuthScreenContent(uiState, snackBarHostState, intentHandler)
}

@Composable
private fun AuthScreenContent(
    uiState: State<AuthUiState>,
    snackBarHostState: SnackbarHostState,
    intentHandler: (AuthIntent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("Welcome")
                MediumTextField("to FlexyPixel")
            }
        },
        bottomBar = {
            val text = when (uiState.value.type) {
                AuthScreenType.LOGIN -> "Forget your password?"
                AuthScreenType.REGISTER -> "Back to login"
            }
            val intent = when (uiState.value.type) {
                AuthScreenType.LOGIN -> AuthIntent.OnResetPasswordClicked
                AuthScreenType.REGISTER -> AuthIntent.OnGoToLoginClicked
            }
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clickable { intentHandler(intent) },
                text = text,
                align = TextAlign.Center,
                color = beigeDark,
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.padding(top = 16.dp)) {
                ProfilePicture(uiState)
            }
            UserCredentials(uiState, intentHandler)
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                FlexyPixelLogo()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProfilePicture(uiState: State<AuthUiState>) {
    val uri = uiState.value.profileUri

    NeoSlotRounded(
        modifier = Modifier.size(160.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uri == null) {
                Image(
                    painter = painterResource(coreDesignR.drawable.ic_profile),
                    contentDescription = null
                )
            } else {
                GlideImage(
                    model = uri,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun UserCredentials(
    uiState: State<AuthUiState>,
    intentHandler: (AuthIntent) -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        NeoSlot(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        ) {
            MediumEditTextField(
                hint = "Login",
                initialText = uiState.value.login,
                modifier = Modifier.fillMaxWidth(),
                indicatorColor = beigeStandard,
                imeAction = ImeAction.Next,
                onValueChanged = { intentHandler(AuthIntent.OnLoginChanged(it)) },
            )
        }
        if (uiState.value.type == AuthScreenType.REGISTER) {
            NeoSlot(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                MediumEditTextField(
                    hint = "Display name",
                    initialText = uiState.value.displayName,
                    modifier = Modifier.fillMaxWidth(),
                    indicatorColor = beigeStandard,
                    imeAction = ImeAction.Next,
                    onValueChanged = { intentHandler(AuthIntent.OnDisplayNameChanged(it)) },
                )
            }
            NeoSlot(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                MediumEditTextField(
                    hint = "Email",
                    initialText = uiState.value.email,
                    modifier = Modifier.fillMaxWidth(),
                    indicatorColor = beigeStandard,
                    imeAction = ImeAction.Next,
                    onValueChanged = { intentHandler(AuthIntent.OnEmailChanged(it)) },
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NeoSlot(modifier = Modifier.weight(1f)) {
                MediumEditTextField(
                    hint = "Password",
                    initialText = uiState.value.password,
                    modifier = Modifier.fillMaxWidth(),
                    indicatorColor = beigeStandard,
                    inputType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChanged = { intentHandler(AuthIntent.OnPasswordChanged(it)) },
                )
            }
            val icon = when (uiState.value.type) {
                AuthScreenType.LOGIN -> R.drawable.ic_done
                AuthScreenType.REGISTER -> R.drawable.ic_add_account
            }
            NeoImageButton(
                imageId = icon,
                scale = 0.8f,
                modifier = Modifier.size(64.dp),
                onClick = {
                    val intent = when (uiState.value.type) {
                        AuthScreenType.LOGIN -> AuthIntent.OnLoginClicked
                        AuthScreenType.REGISTER -> AuthIntent.OnRegisterClicked
                    }
                    intentHandler(intent)
                }
            )
        }
        if (uiState.value.type == AuthScreenType.LOGIN) {
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clickable { intentHandler(AuthIntent.OnGoToRegisterClicked) },
                text = "Or register",
                align = TextAlign.Center,
                color = beigeDark,
            )
        }
    }
}

@Composable
private fun FlexyPixelLogo() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .scale(1.5f),
        painter = painterResource(coreDesignR.drawable.flexy_pixel_logo),
        contentScale = ContentScale.Fit,
        contentDescription = null
    )
}

@Preview
@Composable
private fun AuthScreenContentPreviewEmpty() {
    AppTheme {
        AuthScreenContent(
            uiState = remember { mutableStateOf(AuthUiState()) },
            snackBarHostState = SnackbarHostState()
        )
    }
}

@Preview
@Composable
private fun AuthScreenContentPreview() {
    AppTheme {
        AuthScreenContent(
            uiState = remember {
                mutableStateOf(
                    AuthUiState(
                        type = AuthScreenType.REGISTER,
                        login = "Login",
                        displayName = "My name",
                        password = "123456",
                        email = "email@mail.ru",
                        profileUri = Uri.EMPTY,
                    )
                )
            },
            snackBarHostState = SnackbarHostState()
        )
    }
}