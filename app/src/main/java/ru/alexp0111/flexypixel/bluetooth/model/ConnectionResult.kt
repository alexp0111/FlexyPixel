package ru.alexp0111.flexypixel.bluetooth.model

sealed interface ConnectionResult {
    data object ConnectionEstablished: ConnectionResult
    data class Error(val errorMessage: String): ConnectionResult
}