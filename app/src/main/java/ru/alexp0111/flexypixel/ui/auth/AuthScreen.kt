package ru.alexp0111.flexypixel.ui.auth

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
import ru.alexp0111.flexypixel.ui.auth.model.AuthIntent
import ru.alexp0111.flexypixel.ui.auth.model.AuthUiState
import ru.alexp0111.core_ui.R as coreDesignR

@Composable
internal fun AuthScreen(viewModel: AuthViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    AuthScreenContent(uiState, intentHandler)
}

@Composable
private fun AuthScreenContent(
    uiState: State<AuthUiState>,
    intentHandler: (AuthIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("Welcome")
                MediumTextField("to FlexyPixel")
            }
        },
        bottomBar = {
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clickable { intentHandler(AuthIntent.OnResetPasswordClicked) },
                text = "Forget your password?",
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
            FlexyPixelLogo()
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
                hint = "Username",
                initialText = uiState.value.username,
                modifier = Modifier.fillMaxWidth(),
                indicatorColor = beigeStandard,
                imeAction = ImeAction.Next,
                onValueChanged = { intentHandler(AuthIntent.OnUsernameChanged(it)) },
            )
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
                    initialText = uiState.value.username,
                    modifier = Modifier.fillMaxWidth(),
                    indicatorColor = beigeStandard,
                    imeAction = ImeAction.Next,
                    onValueChanged = { intentHandler(AuthIntent.OnPasswordChanged(it)) },
                )
            }
            NeoImageButton(
                imageId = R.drawable.ic_done,
                scale = 0.8f,
                modifier = Modifier.size(64.dp),
                onClick = { intentHandler(AuthIntent.OnLoginClicked) }
            )
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
                    hint = "Email",
                    initialText = uiState.value.email,
                    modifier = Modifier.fillMaxWidth(),
                    indicatorColor = beigeStandard,
                    onValueChanged = { intentHandler(AuthIntent.OnEmailChanged(it)) },
                )
            }
            NeoImageButton(
                imageId = R.drawable.ic_add_account,
                scale = 0.8f,
                modifier = Modifier.size(64.dp),
                onClick = { intentHandler(AuthIntent.OnRegisterClicked) }
            )
        }
    }
}

@Composable
private fun FlexyPixelLogo() {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .scale(0.25f),
        painter = painterResource(coreDesignR.drawable.flexy_pixel_logo),
        contentDescription = null
    )
}

@Preview
@Composable
private fun AuthScreenContentPreviewEmpty() {
    AppTheme {
        AuthScreenContent(
            uiState = remember { mutableStateOf(AuthUiState()) }
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
                        username = "Username",
                        password = "123456",
                        email = "email@mail.ru",
                        profileUri = Uri.EMPTY,
                    )
                )
            }
        )
    }
}