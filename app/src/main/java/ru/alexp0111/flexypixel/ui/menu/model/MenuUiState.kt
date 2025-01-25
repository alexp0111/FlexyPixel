package ru.alexp0111.flexypixel.ui.menu.model

import androidx.compose.runtime.Immutable

@Immutable
data class MenuUiState(
    val menuItems: List<MenuItem>
)

@Immutable
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