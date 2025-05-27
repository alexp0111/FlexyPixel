package ru.alexp0111.flexypixel.ui.menu

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.menu.model.MenuIntent
import ru.alexp0111.flexypixel.ui.menu.model.MenuItem
import ru.alexp0111.flexypixel.ui.menu.model.MenuItemType
import ru.alexp0111.flexypixel.ui.menu.model.MenuUiState
import ru.alexp0111.remote_repo.profile.repository.IProfileRepository
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val router: Router,
    private val profileRepository: IProfileRepository,
) : MVIViewModel<MenuIntent, MenuUiState, Nothing>() {

    init {
        launch {
            val isDebugger = profileRepository.getUser()?.mobileRole == UserRole.DEVELOPER.name
            setState { copy(isDebugEnabled = isDebugger) }
        }
    }

    override fun setInitialState(): MenuUiState = MenuUiState(
        isDebugEnabled = false,
        menuItems = listOf(
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

    override fun handleIntent(intent: MenuIntent) =
        when (intent) {
            is MenuIntent.MenuItemClicked -> handleMenuItemClick(intent.menuItemType)
        }

    private fun handleMenuItemClick(menuItemType: MenuItemType) =
        when (menuItemType) {
            MenuItemType.NEW_SCHEME -> router.navigateTo(Screens.UpperAbstractionLevelScreen())
            MenuItemType.SAVED_SCHEME -> router.navigateTo(Screens.SavedSchemesScreen())
            MenuItemType.TEMPLATE -> router.navigateTo(Screens.TemplatesScreen())
            MenuItemType.DEBUG -> router.navigateTo(Screens.TestScreen())
            MenuItemType.PROFILE -> router.navigateTo(Screens.ProfileScreen())
        }
}

private enum class UserRole {
    USER, DEVELOPER;
}