package ru.alexp0111.flexypixel.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.alexp0111.core_ui.common.NeoSlotRounded
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.red
import ru.alexp0111.flexypixel.ui.profile.model.ProfileEffect
import ru.alexp0111.flexypixel.ui.profile.model.ProfileIntent
import ru.alexp0111.flexypixel.ui.profile.model.ProfileUiState
import ru.alexp0111.core_ui.R as coreDesignR

@Composable
internal fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is ProfileEffect.ShowSnackBar -> snackBarHostState.showSnackbar(it.message)
            }
        }
    }

    ProfileScreenContent(uiState, snackBarHostState, intentHandler)
}

@Composable
private fun ProfileScreenContent(
    uiState: State<ProfileUiState>,
    snackBarHostState: SnackbarHostState,
    intentHandler: (ProfileIntent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("Profile")
                MediumTextField("details")
            }
        },
        bottomBar = {
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clickable { intentHandler(ProfileIntent.OnExitClicked) },
                text = "Exit account",
                align = TextAlign.Center,
                color = red,
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
            UserCredentials(uiState)
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
private fun ProfilePicture(uiState: State<ProfileUiState>) {
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
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun UserCredentials(
    uiState: State<ProfileUiState>,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        ) {
            SmallTextField(
                text = "Username",
                color = beigeDark
            )
            MediumTextField(
                text = uiState.value.username
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        ) {
            SmallTextField(
                text = "Email",
                color = beigeDark
            )
            MediumTextField(
                text = uiState.value.email
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        ) {
            SmallTextField(
                text = "Register date",
                color = beigeDark
            )
            MediumTextField(
                text = uiState.value.registerDate
            )
        }
    }
}

@Composable
private fun FlexyPixelLogo() {
    Image(
        modifier = Modifier.fillMaxWidth().scale(1.5f),
        painter = painterResource(coreDesignR.drawable.flexy_pixel_logo),
        contentScale = ContentScale.Fit,
        contentDescription = null
    )
}

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    AppTheme {
        ProfileScreenContent(
            uiState = remember {
                mutableStateOf(
                    ProfileUiState(
                        username = "Username",
                        email = "email@mail.ru",
                        registerDate = "12.04.2034",
                        profileUri = Uri.EMPTY,
                    )
                )
            },
            snackBarHostState = SnackbarHostState()
        )
    }
}