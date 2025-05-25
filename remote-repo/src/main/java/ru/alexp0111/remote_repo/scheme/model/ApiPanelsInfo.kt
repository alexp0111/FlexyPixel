package ru.alexp0111.remote_repo.scheme.model

data class ApiPanelsInfo(
    val direction: String,
    val palette: List<ApiPaletteItem>,
    val uid: String,
    val x: Int,
    val y: Int
)

data class ApiPaletteItem(
    val r: Int,
    val g: Int,
    val b: Int,
)