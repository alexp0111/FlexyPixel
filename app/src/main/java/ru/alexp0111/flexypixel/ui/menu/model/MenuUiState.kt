package ru.alexp0111.flexypixel.ui.menu.model

import androidx.compose.runtime.Stable

@Stable
data class MenuUiState(
    val menuItems: List<MenuItem>
)

@Stable
data class MenuItem(
    val menuItemType: MenuItemType,
    val iconId: Int,
    val title: String,
    val subtitle: String,
)

enum class MenuItemType {
    NEW_SCHEME,
    SAVED_SCHEME,
    TEMPLATE;
}