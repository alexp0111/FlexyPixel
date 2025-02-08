package ru.alexp0111.flexypixel.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.ui.menu.model.MenuIntent
import ru.alexp0111.flexypixel.ui.menu.model.MenuItem
import ru.alexp0111.flexypixel.ui.menu.model.MenuItemType
import ru.alexp0111.flexypixel.ui.menu.model.MenuUiState

@Composable
fun MenuScreen(viewModel: MenuViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    MenuScreenContent(uiState, intentHandler)
}

@Composable
private fun MenuScreenContent(
    uiState: State<MenuUiState>,
    intentHandler: (MenuIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
                LargeTextField("CHOOSE")
                MediumTextField("an option")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OptionsList(uiState, intentHandler)
        }
    }
}

@Composable
private fun OptionsList(
    uiState: State<MenuUiState>,
    intentHandler: (MenuIntent) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        uiState.value.menuItems.forEach { menuItem ->
            MenuCard(
                iconId = menuItem.iconId,
                title = menuItem.title,
                subtitle = menuItem.subtitle,
                onClick = { intentHandler(MenuIntent.MenuItemClicked(menuItem.menuItemType)) }
            )
        }
    }
}

@Composable
private fun MenuCard(
    iconId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    NeoCard(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .height(160.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(iconId),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                MediumTextField(title, weight = FontWeight.Bold)
                SmallTextField(subtitle)
            }
        }
    }
}


@Preview
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreenContent(
            uiState = remember {
                mutableStateOf(
                    MenuUiState(
                        listOf(
                            MenuItem(
                                menuItemType = MenuItemType.NEW_SCHEME,
                                iconId = R.drawable.plus_file,
                                title = "CREATE",
                                subtitle = "new scheme"
                            ),
                            MenuItem(
                                menuItemType = MenuItemType.SAVED_SCHEME,
                                iconId = R.drawable.user_file,
                                title = "CHOOSE",
                                subtitle = "saved scheme"
                            ),
                            MenuItem(
                                menuItemType = MenuItemType.TEMPLATE,
                                iconId = R.drawable.star_file,
                                title = "CHOOSE",
                                subtitle = "template"
                            )
                        )
                    )
                )
            }
        )
    }
}