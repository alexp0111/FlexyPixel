package ru.alexp0111.flexypixel.ui.menu.model

sealed interface MenuIntent {
    data class MenuItemClicked(val menuItemType: MenuItemType) : MenuIntent
}