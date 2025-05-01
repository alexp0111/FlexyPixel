package ru.alexp0111.bt_service.model

sealed interface ConnectionResult {
    data object ConnectionEstablished: ConnectionResult
    data class Error(val errorMessage: String): ConnectionResult
}